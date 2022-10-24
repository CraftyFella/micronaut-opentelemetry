package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.opentelemetry.context.Context;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;


@Controller("/happy")
@Slf4j
public class HappyController {
    private final com.example.UnstableThing unstableThing;
    private final Scheduler fred = Schedulers.newParallel("fred");
    private final Scheduler john = Schedulers.newParallel("john");

    public HappyController(com.example.UnstableThing unstableThing) {
        this.unstableThing = unstableThing;
    }

    @Get(uri = "/{name}")
    public Mono<String> index(String name) {
        return Mono.just("ignored")
                .doOnNext(ignored -> {
                    Context ctx = Context.current();
                    log.info("Log in Controller before " + Thread.currentThread());
                })
                .subscribeOn(fred)
                .flatMap(ignored -> unstableThing.GetThingWhichMightBlowUp(name, false))
                .publishOn(john)
                .doOnNext(ignored -> log.info("Log in Controller after " + Thread.currentThread()));
    }
}

