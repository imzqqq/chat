import logging
import threading

from prometheus_client.core import Counter, Histogram

from chat.logging.context import current_context
from chat.metrics import LaterGauge

logger = logging.getLogger(__name__)


# total number of responses served, split by method/servlet/tag
response_count = Counter(
    "synapse_http_server_response_count", "", ["method", "servlet", "tag"]
)

requests_counter = Counter(
    "synapse_http_server_requests_received", "", ["method", "servlet"]
)

outgoing_responses_counter = Counter(
    "synapse_http_server_responses", "", ["method", "code"]
)

response_timer = Histogram(
    "synapse_http_server_response_time_seconds",
    "sec",
    ["method", "servlet", "tag", "code"],
)

response_ru_utime = Counter(
    "synapse_http_server_response_ru_utime_seconds", "sec", ["method", "servlet", "tag"]
)

response_ru_stime = Counter(
    "synapse_http_server_response_ru_stime_seconds", "sec", ["method", "servlet", "tag"]
)

response_db_txn_count = Counter(
    "synapse_http_server_response_db_txn_count", "", ["method", "servlet", "tag"]
)

# seconds spent waiting for db txns, excluding scheduling time, when processing
# this request
response_db_txn_duration = Counter(
    "synapse_http_server_response_db_txn_duration_seconds",
    "",
    ["method", "servlet", "tag"],
)

# seconds spent waiting for a db connection, when processing this request
response_db_sched_duration = Counter(
    "synapse_http_server_response_db_sched_duration_seconds",
    "",
    ["method", "servlet", "tag"],
)

# size in bytes of the response written
response_size = Counter(
    "synapse_http_server_response_size", "", ["method", "servlet", "tag"]
)

# In flight metrics are incremented while the requests are in flight, rather
# than when the response was written.

in_flight_requests_ru_utime = Counter(
    "synapse_http_server_in_flight_requests_ru_utime_seconds", "", ["method", "servlet"]
)

in_flight_requests_ru_stime = Counter(
    "synapse_http_server_in_flight_requests_ru_stime_seconds", "", ["method", "servlet"]
)

in_flight_requests_db_txn_count = Counter(
    "synapse_http_server_in_flight_requests_db_txn_count", "", ["method", "servlet"]
)

# seconds spent waiting for db txns, excluding scheduling time, when processing
# this request
in_flight_requests_db_txn_duration = Counter(
    "synapse_http_server_in_flight_requests_db_txn_duration_seconds",
    "",
    ["method", "servlet"],
)

# seconds spent waiting for a db connection, when processing this request
in_flight_requests_db_sched_duration = Counter(
    "synapse_http_server_in_flight_requests_db_sched_duration_seconds",
    "",
    ["method", "servlet"],
)

# The set of all in flight requests, set[RequestMetrics]
_in_flight_requests = set()

# Protects the _in_flight_requests set from concurrent access
_in_flight_requests_lock = threading.Lock()


def _get_in_flight_counts():
    """Returns a count of all in flight requests by (method, server_name)

    Returns:
        dict[tuple[str, str], int]
    """
    # Cast to a list to prevent it changing while the Prometheus
    # thread is collecting metrics
    with _in_flight_requests_lock:
        reqs = list(_in_flight_requests)

    for rm in reqs:
        rm.update_metrics()

    # Map from (method, name) -> int, the number of in flight requests of that
    # type
    counts = {}
    for rm in reqs:
        key = (rm.method, rm.name)
        counts[key] = counts.get(key, 0) + 1

    return counts


LaterGauge(
    "synapse_http_server_in_flight_requests_count",
    "",
    ["method", "servlet"],
    _get_in_flight_counts,
)


class RequestMetrics:
    def start(self, time_sec, name, method):
        self.start = time_sec
        self.start_context = current_context()
        self.name = name
        self.method = method

        # _request_stats records resource usage that we have already added
        # to the "in flight" metrics.
        self._request_stats = self.start_context.get_resource_usage()

        with _in_flight_requests_lock:
            _in_flight_requests.add(self)

    def stop(self, time_sec, response_code, sent_bytes):
        with _in_flight_requests_lock:
            _in_flight_requests.discard(self)

        context = current_context()

        tag = ""
        if context:
            tag = context.tag

            if context != self.start_context:
                logger.warning(
                    "Context have unexpectedly changed %r, %r",
                    context,
                    self.start_context,
                )
                return

        response_code = str(response_code)

        outgoing_responses_counter.labels(self.method, response_code).inc()

        response_count.labels(self.method, self.name, tag).inc()

        response_timer.labels(self.method, self.name, tag, response_code).observe(
            time_sec - self.start
        )

        resource_usage = context.get_resource_usage()

        response_ru_utime.labels(self.method, self.name, tag).inc(
            resource_usage.ru_utime
        )
        response_ru_stime.labels(self.method, self.name, tag).inc(
            resource_usage.ru_stime
        )
        response_db_txn_count.labels(self.method, self.name, tag).inc(
            resource_usage.db_txn_count
        )
        response_db_txn_duration.labels(self.method, self.name, tag).inc(
            resource_usage.db_txn_duration_sec
        )
        response_db_sched_duration.labels(self.method, self.name, tag).inc(
            resource_usage.db_sched_duration_sec
        )

        response_size.labels(self.method, self.name, tag).inc(sent_bytes)

        # We always call this at the end to ensure that we update the metrics
        # regardless of whether a call to /metrics while the request was in
        # flight.
        self.update_metrics()

    def update_metrics(self):
        """Updates the in flight metrics with values from this request."""
        new_stats = self.start_context.get_resource_usage()

        diff = new_stats - self._request_stats
        self._request_stats = new_stats

        # max() is used since rapid use of ru_stime/ru_utime can end up with the
        # count going backwards due to NTP, time smearing, fine-grained
        # correction, or floating points. Who knows, really?
        in_flight_requests_ru_utime.labels(self.method, self.name).inc(
            max(diff.ru_utime, 0)
        )
        in_flight_requests_ru_stime.labels(self.method, self.name).inc(
            max(diff.ru_stime, 0)
        )

        in_flight_requests_db_txn_count.labels(self.method, self.name).inc(
            diff.db_txn_count
        )

        in_flight_requests_db_txn_duration.labels(self.method, self.name).inc(
            diff.db_txn_duration_sec
        )

        in_flight_requests_db_sched_duration.labels(self.method, self.name).inc(
            diff.db_sched_duration_sec
        )
