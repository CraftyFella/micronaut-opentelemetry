package com.example;

import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;


@Singleton
@Slf4j
public class SlowThing {


    private HttpBinClient client1;
    private OtherSystemClient client2;

    public SlowThing(OtherSystemClient client1, HttpBinClient client2) {
        this.client2 = client1;
        this.client1 = client2;
    }

    @NewSpan("slowThing")
    public String GetSlowThing(@SpanTag("thing") String thing) {
        try {
            log.info("Info before sleep");
            Thread.sleep(300);
            // Throw an exception ~25% of the time
//            if (new Random().nextInt(4) == 0) {
//                throw new IllegalStateException("Error!");
//            }
            client1.get(200);
            client2.stock("1680502395");

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

