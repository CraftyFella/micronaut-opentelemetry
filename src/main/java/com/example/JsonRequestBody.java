package com.example;

import io.opentelemetry.exporter.internal.marshal.Marshaler;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

import java.io.IOException;

public final class JsonRequestBody extends RequestBody {
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    private final Marshaler marshaler;

    public JsonRequestBody(Marshaler marshaler) {
        this.marshaler = marshaler;
    }

    @Override
    public long contentLength() {
        return -1;
    }

    @Override
    public MediaType contentType() {
        return JSON_MEDIA_TYPE;
    }

    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {
        marshaler.writeJsonTo(bufferedSink.outputStream());
    }
}
