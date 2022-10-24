package com.example;

import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.reactor.http.client.ReactorHttpClient;
import io.opentelemetry.api.trace.Span;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Singleton
public class ReactiveDemoClient {
    private final ReactorHttpClient httpClient;

    public Mono<HttpStatus> get() {
        MutableHttpRequest<Object> request = HttpRequest.create(HttpMethod.GET, "https://httpbin.org/status/200");
        return httpClient.exchange(request)
                .doOnNext(ignored -> {
                    Span.current().setAttribute("custom-attribute4", Thread.currentThread().getName());
                })
                .next()
                .map(resp -> resp.status());
    }
}
