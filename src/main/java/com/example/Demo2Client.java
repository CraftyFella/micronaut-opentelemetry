package com.example;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("https://httpbin.org")
public interface Demo2Client {
    @Get("/status/{statusCode}")
    void get(int statusCode);
}
