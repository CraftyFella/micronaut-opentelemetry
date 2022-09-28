package com.example.opentelemetry;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.opentelemetry.api.trace.Span;
import org.reactivestreams.Publisher;

@Filter("/**")
public class TracingFilter implements HttpServerFilter {


    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {

        Publisher<MutableHttpResponse<?>> proceed = chain.proceed(request);

        return Publishers.map(proceed, response -> {
            response.header("TraceId", Span.current().getSpanContext().getTraceId());
            return response;
        });

    }


}
