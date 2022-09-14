package com.example;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("http://localhost:8082")
public interface OtherSystemClient {
    @Get("/books/stock/{isbn}")
    void stock(String isbn);
}
