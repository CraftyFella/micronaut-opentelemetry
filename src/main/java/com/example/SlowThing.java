package com.example;

import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import io.opentracing.Tracer;
import jakarta.inject.Singleton;

@Singleton
public class SlowThing {

    private final Tracer tracer;

    public SlowThing(Tracer tracer) {
        this.tracer = tracer;
    }

    @NewSpan("slowThing")
    public String GetSlowThing(@SpanTag("thing") String thing) {
        try {
            var activeSpan = tracer.activeSpan();
            activeSpan.setTag("Dave", "was here");
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Sllooooow " + thing;
    }
}

