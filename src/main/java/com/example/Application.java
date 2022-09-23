package com.example;

import io.micronaut.runtime.Micronaut;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
    public static void main(String[] args) {

        var settings = LoggingExporterSettings.getDefault().withServiceName("dave.demo.tracing2");
        OpenTelemetry.addLoggingExporter(settings);

        log.info("hey i'm running with args " + String.join(",", args));

        Micronaut.run(Application.class, args);
    }

}


