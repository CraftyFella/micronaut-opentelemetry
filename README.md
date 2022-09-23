## Demo of Micronaut and OTLP instrumentation for New Relic

A working example of using Micronaut and open telemetry that pushed traces, metrics and logs to new relic. (could be any open telemetry provider)

Working:

* Spans and Traces being sent to OTLP server
* Metrics being sent to OTLP server
* Logs being sent to OTLP server

Not Working:
* Baggage doesn't seem to be being sent

## To run the demo:

Set these environment variables:

```
export OTEL_EXPORTER_OTLP_HEADERS=api-key=your_license_key \
&& export NEW_RELIC_KEY=your_license_key
&& export OTEL_METRIC_EXPORT_INTERVAL=5000 \
&& export OTEL_EXPORTER_OTLP_ENDPOINT=https://otlp.nr-data.net:4317 \
&& export OTEL_EXPORTER_OTLP_COMPRESSION=gzip \
&& export OTEL_EXPERIMENTAL_EXPORTER_OTLP_RETRY_ENABLED=true \
&& export OTEL_SPAN_ATTRIBUTE_VALUE_LENGTH_LIMIT=4095
```

Replace `your_license_key` with your [Account License Key](https://one.newrelic.com/launcher/api-keys-ui.launcher).

Launch the application with :
```
./gradlew run
curl http://localhost:8080/tracingDem 
```
