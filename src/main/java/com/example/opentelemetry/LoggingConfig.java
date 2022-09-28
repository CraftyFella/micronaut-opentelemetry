package com.example.opentelemetry;

import io.micronaut.context.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoggingConfig {

    @Value("${micronaut.application.name}")
    protected String name;

    @Value("${otel.exporter.otlp.endpoint}") //
    private String endpoint;

    @Value("${otel.exporter.otlp.key}") //
    private String key;

    @Value("${otel.exporter.otlp.instance}") //
    private String instance;

}
