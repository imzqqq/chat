import logging
from sys import intern
from typing import Callable, Dict, Optional, Sized

import attr
from prometheus_client.core import Gauge

from chat.config.cache import add_resizable_cache

logger = logging.getLogger(__name__)


# Whether to track estimated memory usage of the LruCaches.
TRACK_MEMORY_USAGE = False


caches_by_name: Dict[str, Sized] = {}
collectors_by_name: Dict[str, "CacheMetric"] = {}

cache_size = Gauge("synapse_util_caches_cache:size", "", ["name"])
cache_hits = Gauge("synapse_util_caches_cache:hits", "", ["name"])
cache_evicted = Gauge("synapse_util_caches_cache:evicted_size", "", ["name"])
cache_total = Gauge("synapse_util_caches_cache:total", "", ["name"])
cache_max_size = Gauge("synapse_util_caches_cache_max_size", "", ["name"])
cache_memory_usage = Gauge(
    "synapse_util_caches_cache_size_bytes",
    "Estimated memory usage of the caches",
    ["name"],
)

response_cache_size = Gauge("synapse_util_caches_response_cache:size", "", ["name"])
response_cache_hits = Gauge("synapse_util_caches_response_cache:hits", "", ["name"])
response_cache_evicted = Gauge(
    "synapse_util_caches_response_cache:evicted_size", "", ["name"]
)
response_cache_total = Gauge("synapse_util_caches_response_cache:total", "", ["name"])


@attr.s(slots=True)
class CacheMetric:

    _cache = attr.ib()
    _cache_type = attr.ib(type=str)
    _cache_name = attr.ib(type=str)
    _collect_callback = attr.ib(type=Optional[Callable])

    hits = attr.ib(default=0)
    misses = attr.ib(default=0)
    evicted_size = attr.ib(default=0)
    memory_usage = attr.ib(default=None)

    def inc_hits(self):
        self.hits += 1

    def inc_misses(self):
        self.misses += 1

    def inc_evictions(self, size=1):
        self.evicted_size += size

    def inc_memory_usage(self, memory: int):
        if self.memory_usage is None:
            self.memory_usage = 0

        self.memory_usage += memory

    def dec_memory_usage(self, memory: int):
        self.memory_usage -= memory

    def clear_memory_usage(self):
        if self.memory_usage is not None:
            self.memory_usage = 0

    def describe(self):
        return []

    def collect(self):
        try:
            if self._cache_type == "response_cache":
                response_cache_size.labels(self._cache_name).set(len(self._cache))
                response_cache_hits.labels(self._cache_name).set(self.hits)
                response_cache_evicted.labels(self._cache_name).set(self.evicted_size)
                response_cache_total.labels(self._cache_name).set(
                    self.hits + self.misses
                )
            else:
                cache_size.labels(self._cache_name).set(len(self._cache))
                cache_hits.labels(self._cache_name).set(self.hits)
                cache_evicted.labels(self._cache_name).set(self.evicted_size)
                cache_total.labels(self._cache_name).set(self.hits + self.misses)
                if getattr(self._cache, "max_size", None):
                    cache_max_size.labels(self._cache_name).set(self._cache.max_size)

                if TRACK_MEMORY_USAGE:
                    # self.memory_usage can be None if nothing has been inserted
                    # into the cache yet.
                    cache_memory_usage.labels(self._cache_name).set(
                        self.memory_usage or 0
                    )
            if self._collect_callback:
                self._collect_callback()
        except Exception as e:
            logger.warning("Error calculating metrics for %s: %s", self._cache_name, e)
            raise


def register_cache(
    cache_type: str,
    cache_name: str,
    cache: Sized,
    collect_callback: Optional[Callable] = None,
    resizable: bool = True,
    resize_callback: Optional[Callable] = None,
) -> CacheMetric:
    """Register a cache object for metric collection and resizing.

    Args:
        cache_type: a string indicating the "type" of the cache. This is used
            only for deduplication so isn't too important provided it's constant.
        cache_name: name of the cache
        cache: cache itself, which must implement __len__(), and may optionally implement
             a max_size property
        collect_callback: If given, a function which is called during metric
            collection to update additional metrics.
        resizable: Whether this cache supports being resized, in which case either
            resize_callback must be provided, or the cache must support set_max_size().
        resize_callback: A function which can be called to resize the cache.

    Returns:
        CacheMetric: an object which provides inc_{hits,misses,evictions} methods
    """
    if resizable:
        if not resize_callback:
            resize_callback = cache.set_cache_factor  # type: ignore
        add_resizable_cache(cache_name, resize_callback)

    metric = CacheMetric(cache, cache_type, cache_name, collect_callback)
    metric_name = "cache_%s_%s" % (cache_type, cache_name)
    caches_by_name[cache_name] = cache
    collectors_by_name[metric_name] = metric
    return metric


KNOWN_KEYS = {
    key: key
    for key in (
        "auth_events",
        "content",
        "depth",
        "event_id",
        "hashes",
        "origin",
        "origin_server_ts",
        "prev_events",
        "room_id",
        "sender",
        "signatures",
        "state_key",
        "type",
        "unsigned",
        "user_id",
    )
}


def intern_string(string):
    """Takes a (potentially) unicode string and interns it if it's ascii"""
    if string is None:
        return None

    try:
        return intern(string)
    except UnicodeEncodeError:
        return string


def intern_dict(dictionary):
    """Takes a dictionary and interns well known keys and their values"""
    return {
        KNOWN_KEYS.get(key, key): _intern_known_values(key, value)
        for key, value in dictionary.items()
    }


def _intern_known_values(key, value):
    intern_keys = ("event_id", "room_id", "sender", "user_id", "type", "state_key")

    if key in intern_keys:
        return intern_string(value)

    return value
