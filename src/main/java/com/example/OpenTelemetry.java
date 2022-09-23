package com.example;

import io.opentelemetry.exporter.internal.retry.RetryPolicy;
import io.opentelemetry.exporter.internal.retry.RetryUtil;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.logs.LogLimits;
import io.opentelemetry.sdk.logs.SdkLogEmitterProvider;
import io.opentelemetry.sdk.logs.export.BatchLogProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

public class OpenTelemetry {
    public static void addLoggingExporter(LoggingExporterSettings settings) {
        var resource = Resource.getDefault().toBuilder()
                .put(ResourceAttributes.SERVICE_NAME, settings.serviceName())
                .put(ResourceAttributes.SERVICE_INSTANCE_ID, settings.serviceInstanceId())
                .build();
        ;

        if (settings.newRelicKey() == null) {
            throw new RuntimeException("ooops");
        }
        var logExporterBuilder =
                OtlpGrpcLogExporter.builder()
                        .setEndpoint(settings.otlpEndpoint())
                        .setCompression("gzip")
                        .addHeader("api-key", settings.newRelicKey());


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