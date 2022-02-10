This directory contains some sample monitoring config for using the
'Prometheus' monitoring server against chat.

To use it, first install prometheus by following the instructions at

  http://prometheus.io/

### for Prometheus v1

Add a new job to the main prometheus.conf file:

```yaml
  job: {
    name: "chat"

    target_group: {
      target: "http://SERVER.LOCATION.HERE:PORT/_chat/metrics"
    }
  }
```

### for Prometheus v2

Add a new job to the main prometheus.yml file:

```yaml
  - job_name: "chat"
    metrics_path: "/_chat/metrics"
    # when endpoint uses https:
    scheme: "https"

    static_configs:
    - targets: ["my.server.here:port"]
```

An example of a Prometheus configuration with workers can be found in
[metrics-howto.md](https://chat.docs.dingshunyu.top/metrics-howto.html).

To use `chat.rules` add

```yaml
  rule_files:
    - "/PATH/TO/chat-v2.rules"
```

Metrics are disabled by default when running chat; they must be enabled
with the 'enable-metrics' option, either in the chat config file or as a
command-line option.
