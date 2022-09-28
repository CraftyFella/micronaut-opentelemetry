package com.example;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("http://localhost:8083")
public interface Demo2Client {
    @Get("/demo2/{name}/{blowUp}")
    String get(String name, Boolean blowUp);
}
