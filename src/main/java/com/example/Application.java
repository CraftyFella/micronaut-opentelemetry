package com.example;

import io.micronaut.runtime.Micronaut;

public class Application {
    public static void main(String[] args) {

        System.out.println("hey i'm running with args " + String.join(",", args));
        Micronaut.run(Application.class, args);
    }
}
