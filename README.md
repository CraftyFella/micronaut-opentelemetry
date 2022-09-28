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
export NEW_RELIC_KEY=your_license_key
```

Replace `your_license_key` with your [Account License Key](https://one.newrelic.com/launcher/api-keys-ui.launcher).

Launch the application with :
```
./gradlew run
curl http://localhost:8080/happy/path 
curl http://localhost:8080/sad/path 
```
