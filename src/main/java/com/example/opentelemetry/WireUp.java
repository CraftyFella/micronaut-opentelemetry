package com.example.opentelemetry;

import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.opentelemetry.exporter.internal.retry.RetryPolicy;
import io.opentelemetry.exporter.internal.retry.RetryUtil;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.logs.LogLimits;
import io.opentelemetry.sdk.logs.SdkLogEmitterProvider;
import io.opentelemetry.sdk.logs.export.BatchLogProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;


@Singleton
@AllArgsConstructor
public class WireUp {
    @Inject
    private final LoggingConfig config;

    @EventListener
    void startupHandler(ServerStartupEvent event) {

        addLoggingExporter(config);
    }

    public static void addLoggingExporter(LoggingConfig config) {

        var resource = Resource.getDefault().toBuilder()
                .put(ResourceAttributes.SERVICE_NAME, config.getName())
                .put(ResourceAttributes.SERVICE_INSTANCE_ID, config.getInstance())
                .build();

        var logExporterBuilder =
                OtlpGrpcLogExporter.builder()
                        .setEndpoint(config.getEndpoint())
                        .setCompression("gzip")
                        .addHeader("api-key", config.getKey());

        RetryUtil.setRetryPolicyOnDelegate(logExporterBuilder, RetryPolicy.getDefault());

        SdkLogEmitterProvider logEmitterProvider =
                SdkLogEmitterProvider.builder()
                        .setResource(resource)
                        .setLogLimits(() -> LogLimits.getDefault().toBuilder().setMaxAttributeValueLength(4095).build())
                        .addLogProcessor(BatchLogProcessor.builder(logExporterBuilder.build()).build())
                        .build();

        OpenTelemetryAppender.setSdkLogEmitterProvider(logEmitterProvider);
    }
}