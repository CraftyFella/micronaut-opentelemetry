package com.example;

import io.micronaut.runtime.Micronaut;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.logs.SdkLogEmitterProvider;
import io.opentelemetry.sdk.logs.export.SimpleLogProcessor;
import io.opentelemetry.sdk.resources.Resource;

public class Application {
    public static void main(String[] args) {

        System.out.println("hey i'm running with args " + String.join(",", args));

        var logExporter = OtlpHttpLogExporter
                .builder()
                .setEndpoint(System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT"))
                .setCompression(System.getenv("OTEL_EXPORTER_OTLP_COMPRESSION"))
                .addHeader("api-key", "NEW_RELIC_KEY")
                //.asJson()
                .build();

        var resource = Resource.getDefault();

        SdkLogEmitterProvider logEmitterProvider =
                SdkLogEmitterProvider.builder()
                        .setResource(resource)
                        .addLogProcessor(SimpleLogProcessor.create(logExporter))
                        .build();

        OpenTelemetryAppender.setSdkLogEmitterProvider(logEmitterProvider);

        Micronaut.run(Application.class, args);
    }
}
