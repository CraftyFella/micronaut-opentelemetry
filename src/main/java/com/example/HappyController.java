package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

@Controller("/happy")
@Slf4j
public class HappyController {
    private final com.example.UnstableThing unstableThing;

    public HappyController(com.example.UnstableThing unstableThing) {
        this.unstableThing = unstableThing;
    }

    @Get(uri="/{name}")
    public String index(String name) {
        log.info("Log in Controller before");
        String dave = unstableThing.GetThingWhichMightBlowUp(name, false);
        log.info("Log in Controller after");
        return dave;
    }
}

