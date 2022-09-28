package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

@Controller("/sad")
@Slf4j
public class SadController {
    private final UnstableThing unstableThing;

    public SadController(UnstableThing unstableThing) {
        this.unstableThing = unstableThing;
    }

    @Get(uri="/{name}")
    public String index(String name) {
        log.info("Log in Controller before");
        String dave = unstableThing.GetThingWhichMightBlowUp(name, true);
        log.info("Log in Controller after");
        return dave;
    }
}

