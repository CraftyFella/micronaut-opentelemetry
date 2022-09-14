## Demo of Micronaut and OTLP instrumentation for New Relic

Eventually I'd like this to be an example of how to configure micronaut with open telemetry. Right now it's in a semi working state.

Working:

* Spans and Traces being sent to OTLP server

Not working:

* Logs being sent to OTLP server
* Metrics being sent to OTLP server

## To run the demo:

Set these environment variables:

```
export OTEL_EXPORTER_OTLP_HEADERS=api-key=your_license_key \
&& export OTEL_METRIC_EXPORT_INTERVAL=5000 \
&& export OTEL_EXPORTER_OTLP_METRICS_TEMPORALITY_PREFERENCE=DELTA \
&& export OTEL_EXPORTER_OTLP_METRICS_DEFAULT_HISTOGRAM_AGGREGATION=EXPONENTIAL_BUCKET_HISTOGRAM \
&& export OTEL_LOGS_EXPORTER=otlp \
&& export OTEL_EXPORTER_OTLP_ENDPOINT=https://otlp.nr-data.net:4317 \
&& export OTEL_EXPORTER_OTLP_COMPRESSION=gzip \
&& export OTEL_EXPERIMENTAL_EXPORTER_OTLP_RETRY_ENABLED=true \
&& export OTEL_SERVICE_NAME=OTLP-demo-cli \
&& export OTEL_RESOURCE_ATTRIBUTES=service.instance.id=1234 \
&& export OTEL_JAVA_DISABLED_RESOURCE_PROVIDERS=io.opentelemetry.sdk.extension.resources.ProcessResourceProvider \
&& export OTEL_SPAN_ATTRIBUTE_VALUE_LENGTH_LIMIT=4095
```

Replace `your_license_key` with your [Account License Key](https://one.newrelic.com/launcher/api-keys-ui.launcher).

Launch the application with :
```
./gradlew run
curl http://localhost:8080/tracingDem 
```

## What's working and what's not working
