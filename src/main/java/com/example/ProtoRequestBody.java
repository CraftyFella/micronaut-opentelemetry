/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.example;

import io.opentelemetry.exporter.internal.marshal.Marshaler;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

import java.io.IOException;

final class ProtoRequestBody extends RequestBody {

    private static final MediaType PROTOBUF_MEDIA_TYPE = MediaType.parse("application/x-protobuf");

    private final Marshaler marshaler;
    private final int contentLength;

    public ProtoRequestBody(Marshaler marshaler) {
        this.marshaler = marshaler;
        contentLength = marshaler.getBinarySerializedSize();
    }

    @Override
    public long contentLength() {
        return contentLength;
    }

    @Override
    public MediaType contentType() {
        return PROTOBUF_MEDIA_TYPE;
    }

    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {
        marshaler.writeBinaryTo(bufferedSink.outputStream());
    }
}

