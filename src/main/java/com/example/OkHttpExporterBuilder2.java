package com.example;

import io.micronaut.core.annotation.Nullable;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.exporter.internal.ExporterBuilderUtil;
import io.opentelemetry.exporter.internal.TlsUtil;
import io.opentelemetry.exporter.internal.marshal.Marshaler;
import io.opentelemetry.exporter.internal.okhttp.OkHttpUtil;
import io.opentelemetry.exporter.internal.retry.RetryInterceptor;
import io.opentelemetry.exporter.internal.retry.RetryPolicy;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLException;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class OkHttpExporterBuilder2<T extends Marshaler> {
    public static final long DEFAULT_TIMEOUT_SECS = 10;

    private final String exporterName;
    private final String type;

    private String endpoint;

    private long timeoutNanos = TimeUnit.SECONDS.toNanos(DEFAULT_TIMEOUT_SECS);
    private boolean compressionEnabled = false;
    private boolean exportAsJson = false;
    @Nullable
    private Headers.Builder headersBuilder;
    @Nullable
    private byte[] trustedCertificatesPem;
    @Nullable
    private byte[] privateKeyPem;
    @Nullable
    private byte[] certificatePem;
    @Nullable
    private RetryPolicy retryPolicy;
    private MeterProvider meterProvider = MeterProvider.noop();

    public OkHttpExporterBuilder2(String exporterName, String type, String defaultEndpoint) {
        this.exporterName = exporterName;
        this.type = type;

        endpoint = defaultEndpoint;
    }

    public OkHttpExporterBuilder2<T> setTimeout(long timeout, TimeUnit unit) {
        timeoutNanos = unit.toNanos(timeout);
        return this;
    }

    public OkHttpExporterBuilder2<T> setTimeout(Duration timeout) {
        return setTimeout(timeout.toNanos(), TimeUnit.NANOSECONDS);
    }

    public OkHttpExporterBuilder2<T> setEndpoint(String endpoint) {
        URI uri = ExporterBuilderUtil.validateEndpoint(endpoint);
        this.endpoint = uri.toString();
        return this;
    }

    public OkHttpExporterBuilder2<T> setCompression(String compressionMethod) {
        if (compressionMethod.equals("gzip")) {
            this.compressionEnabled = true;
        }
        return this;
    }

    public OkHttpExporterBuilder2<T> addHeader(String key, String value) {
        if (headersBuilder == null) {
            headersBuilder = new Headers.Builder();
        }
        headersBuilder.add(key, value);
        return this;
    }

    public OkHttpExporterBuilder2<T> setTrustedCertificates(byte[] trustedCertificatesPem) {
        this.trustedCertificatesPem = trustedCertificatesPem;
        return this;
    }

    public OkHttpExporterBuilder2<T> setClientTls(byte[] privateKeyPem, byte[] certificatePem) {
        this.privateKeyPem = privateKeyPem;
        this.certificatePem = certificatePem;
        return this;
    }

    public OkHttpExporterBuilder2<T> setMeterProvider(MeterProvider meterProvider) {
        this.meterProvider = meterProvider;
        return this;
    }

    public OkHttpExporterBuilder2<T> setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public OkHttpExporterBuilder2<T> exportAsJson() {
        this.exportAsJson = true;
        return this;
    }

    public OkHttpExporter2<T> build() {

        var proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888));

        OkHttpClient.Builder clientBuilder =
                new OkHttpClient.Builder()
                        .proxy(proxy)
                        .dispatcher(OkHttpUtil.newDispatcher())
                        .callTimeout(Duration.ofNanos(timeoutNanos));

        if (trustedCertificatesPem != null) {
            try {
                X509TrustManager trustManager = TlsUtil.trustManager(trustedCertificatesPem);
                X509KeyManager keyManager = null;
                if (privateKeyPem != null && certificatePem != null) {
                    keyManager = TlsUtil.keyManager(privateKeyPem, certificatePem);
                }
                clientBuilder.sslSocketFactory(
                        TlsUtil.sslSocketFactory(keyManager, trustManager), trustManager);
            } catch (SSLException e) {
                throw new IllegalStateException(
                        "Could not set trusted certificate for OTLP HTTP connection, are they valid X.509 in PEM format?",
                        e);
            }
        }

        Headers headers = headersBuilder == null ? null : headersBuilder.build();

        if (retryPolicy != null) {
            clientBuilder.addInterceptor(new RetryInterceptor(retryPolicy, OkHttpExporter2::isRetryable));
        }

        return new OkHttpExporter2<>(
                exporterName,
                type,
                clientBuilder.build(),
                meterProvider,
                endpoint,
                headers,
                compressionEnabled,
                exportAsJson);
    }
}
