package com.example;

import java.util.UUID;

public record LoggingExporterSettings(String serviceName, String newRelicKey, String otlpEndpoint,
                                      String serviceInstanceId) {

    public static LoggingExporterSettings getDefault() {
        return new LoggingExporterSettings("my-app", System.getenv("NEW_RELIC_KEY"), System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT"), UUID.randomUUID().toString());
    }

    public LoggingExporterSettings withServiceName(String name) {
        return new LoggingExporterSettings(name, this.newRelicKey, this.otlpEndpoint, this.serviceInstanceId);
    }
}
