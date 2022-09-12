## Demo of Micronaut and OTLP instrumentation for New Relic

Set these environment variables:

```
export OTEL_EXPORTER_OTLP_HEADERS=api-key=your_license_key \
> && export OTEL_METRIC_EXPORT_INTERVAL=5000 \
> && export OTEL_EXPORTER_OTLP_METRICS_TEMPORALITY_PREFERENCE=DELTA \
> && export OTEL_EXPORTER_OTLP_METRICS_DEFAULT_HISTOGRAM_AGGREGATION=EXPONENTIAL_BUCKET_HISTOGRAM \
> && export OTEL_LOGS_EXPORTER=otlp \
> && export OTEL_EXPORTER_OTLP_ENDPOINT=https://otlp.nr-data.net:4317 \
> && export OTEL_EXPORTER_OTLP_COMPRESSION=gzip \
> && export OTEL_EXPERIMENTAL_EXPORTER_OTLP_RETRY_ENABLED=true \
> && export OTEL_SERVICE_NAME=OTLP-demo-cli \
> && export OTEL_RESOURCE_ATTRIBUTES=service.instance.id=1234 \
> && export OTEL_JAVA_DISABLED_RESOURCE_PROVIDERS=io.opentelemetry.sdk.extension.resources.ProcessResourceProvider \
> && export OTEL_SPAN_ATTRIBUTE_VALUE_LENGTH_LIMIT=4095
```

Replace `your_license_key` with your [Account License Key](https://one.newrelic.com/launcher/api-keys-ui.launcher).

Download the OTLP agent with
```
gradle downloadAgent
```

Launch the application with :
```
java  -javaagent:build/otel/opentelemetry-javaagent-all-1.17.0.jar -jar build/libs/demoOTLP-0.1-all.jar 
```

Review distributed tracing in New Relic for the specified application, specifically:
* Span Attributes do not contain the expected tag : `Dave`
* Only a single Span is sent to new relic even though we have a Custom Span `slowThing`

