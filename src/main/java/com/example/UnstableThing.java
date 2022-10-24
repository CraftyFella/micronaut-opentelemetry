package com.example;


import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


@Singleton
@Slf4j
public class UnstableThing {

    private static final Meter sampleMeter =
            GlobalOpenTelemetry.getMeter("io.opentelemetry.example.metrics");

    private static final LongCounter aCounter =
            sampleMeter
                    .counterBuilder("a_counter")
                    .setDescription("Counts Stuff")
                    .setUnit("unit")
                    .build();

    private ReactiveDemoClient demoClient;

    public UnstableThing(ReactiveDemoClient demo2Client) {
        this.demoClient = demo2Client;
    }

    @NewSpan("slowThing")
    public Mono<String> GetThingWhichMightBlowUp(@SpanTag("thing") String thing,
                                                 Boolean bang) {

        Context ctx = Context.current();
        Span current = Span.current();

        current.setAttribute("custom-attribute1", Thread.currentThread().getName());
//
//        Baggage.current()
//                .toBuilder()
//                .put("app.username", "aUser")
//                .build()
//                .makeCurrent();

        aCounter.add(1);

        return Mono.just("start")
                .doOnNext(ignored -> {
                    log.info("Info before sleep " + Thread.currentThread());
                    current.setAttribute("custom-attribute2", Thread.currentThread().getName());
                    aCounter.add(1);
                })
                .delaySubscription(Duration.of(300, ChronoUnit.MILLIS))
                .doOnNext(ignored -> {
                    log.warn("warn after sleep " + Thread.currentThread());
                    current.setAttribute("custom-attribute3", Thread.currentThread().getName());
                    aCounter.add(1);
                })
                .flatMap(ignored -> demoClient.get())
                .map(status -> Integer.toString(status.getCode()))
                .doOnNext(ignored -> {
                    log.warn("all done " + Thread.currentThread());
                    aCounter.add(1);
                });
    }
}

