package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/tracingDem")
public class TracingDemController {
    private final SlowThing slowThing;

    public TracingDemController(SlowThing slowThing) {
        this.slowThing = slowThing;
    }

    @Get(uri="/", produces="text/plain")
    public String index() {
        return slowThing.GetSlowThing("dave");
    }
}

