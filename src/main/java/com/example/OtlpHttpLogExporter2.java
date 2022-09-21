package com.example;

import io.opentelemetry.exporter.internal.otlp.logs.LogsRequestMarshaler;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.logs.data.LogData;
import io.opentelemetry.sdk.logs.export.LogExporter;

import java.util.Collection;

public final class OtlpHttpLogExporter2 implements LogExporter {

    private final OkHttpExporter2<LogsRequestMarshaler> delegate;

    OtlpHttpLogExporter2(OkHttpExporter2<LogsRequestMarshaler> delegate) {
        this.delegate = delegate;
    }

    /**
     * Returns a new {@link OtlpHttpLogExporter2} using the default values.
     *
     * @return a new {@link OtlpHttpLogExporter2} instance.
     */
    public static OtlpHttpLogExporter2 getDefault() {
        return builder().build();
    }

    /**
     * Returns a new builder instance for this exporter.
     *
     * @return a new builder instance for this exporter.
     */
    public static OtlpHttpLogExporterBuilder2 builder() {
        return new OtlpHttpLogExporterBuilder2();
    }

    /**
     * Submits all the given logs in a single batch to the OpenTelemetry collector.
     *
     * @param logs the list of sampled Logs to be exported.
     * @return the result of the operation
     */
    @Override
    public CompletableResultCode export(Collection<LogData> logs) {
        LogsRequestMarshaler exportRequest = LogsRequestMarshaler.create(logs);
        return delegate.export(exportRequest, logs.size());
    }

    @Override
    public CompletableResultCode flush() {
        return CompletableResultCode.ofSuccess();
    }

    /**
     * Shutdown the exporter.
     */
    @Override
    public CompletableResultCode shutdown() {
        return delegate.shutdown();
    }
}
