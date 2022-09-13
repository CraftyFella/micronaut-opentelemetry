package com.example;

import io.opentelemetry.extension.annotations.SpanAttribute;
import io.opentelemetry.extension.annotations.WithSpan;
import jakarta.inject.Singleton;

@Singleton
public class SlowThing {


    public SlowThing() {
    }

    @WithSpan("slowThing")
    public String GetSlowThing(@SpanAttribute("thing") String thing) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Sllooooow " + thing;
    }
}

