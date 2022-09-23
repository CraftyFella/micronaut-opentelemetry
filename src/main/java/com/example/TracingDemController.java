package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

@Controller("/tracingDem2")
@Slf4j
public class TracingDemController {
    private final com.example.UnstableThing unstableThing;

    public TracingDemController(com.example.UnstableThing unstableThing) {
        this.unstableThing = unstableThing;
    }

    @Get(uri="/{name}/{bang}")
    public String index(String name, Boolean bang) {
        log.info("Log in Controller before");
        String dave = unstableThing.GetThingWhichMightBlowUp(name, bang);
        log.info("Log in Controller after");
        return dave;
    }
}

