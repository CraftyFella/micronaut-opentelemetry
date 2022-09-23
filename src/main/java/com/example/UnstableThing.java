package com.example;


import io.micronaut.tracing.annotation.SpanTag;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;


@Singleton
@Slf4j
public class UnstableThing {
    private Demo2Client demo2Client;

    public UnstableThing(Demo2Client demo2Client) {
        this.demo2Client = demo2Client;
    }

    @WithSpan("slowThing")
    public String GetThingWhichMightBlowUp(@SpanTag("thing") String thing, Boolean bang) {

        Span current = Span.current();

        current.setAttribute("custom-attribute", "custom-attribute");

        Baggage.current()
                .toBuilder()
                .put("app.username", "aUser")
                .build()
                .makeCurrent();

        try {
            log.info("Info before sleep");
            Thread.sleep(300);

            if (bang){
                throw new RuntimeException("boom baby");
            }

            log.warn("warn after sleep");
            String response = demo2Client.get(thing);
            return "From DB "  + response;

        } catch (InterruptedException e) {
            log.error("error caught", e);
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            log.error("error caught", e);
            throw new RuntimeException(e);
        }
    }
}
