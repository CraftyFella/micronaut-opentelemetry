package com.example;

import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;


@Singleton
@Slf4j
public class SlowThing {
    private HttpBinClient httpBinClient;

    public SlowThing(HttpBinClient httpBinClient) {
        this.httpBinClient = httpBinClient;
    }

    @NewSpan("slowThing")
    public String GetSlowThing(@SpanTag("thing") String thing) {
        try {
            log.info("Info before sleep");
            Thread.sleep(300);
            httpBinClient.get(200);
            log.warn("warn after sleep");
        } catch (InterruptedException e) {
            log.error("error caught", e);
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            log.error("error caught", e);
            throw new RuntimeException(e);
        }
        return "Sllooooow " + thing;
    }
}

