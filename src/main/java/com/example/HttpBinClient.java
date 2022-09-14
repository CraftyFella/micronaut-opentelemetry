package com.example;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("https://httpbin.org")
public interface HttpBinClient {
    @Get("/status/{status}")
    void get(int status);
}
