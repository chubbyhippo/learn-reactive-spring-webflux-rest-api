package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformTest {
    List<String> animals = Arrays.asList("dog", "cat", "bird");

    @Test
    public void transformUsingMapTest() {
        Flux<String> stringFlux = Flux.fromIterable(animals)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("DOG", "CAT", "BIRD")
                .verifyComplete();

    }

    @Test
    public void transformUsingMapLengthTest() {

        Flux<Integer> integerFlux = Flux.fromIterable(animals)
                .map(String::length)
                .log();

        StepVerifier.create(integerFlux)
                .expectNext(3, 3, 4)
                .verifyComplete();
    }

}