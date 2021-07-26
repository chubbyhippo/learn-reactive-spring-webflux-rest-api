package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFactoryTest {
    List<String> animals = Arrays.asList("dog", "cat", "bird");

    @Test
    public void fluxUsingIterableTest() {
        Flux<String> stringFlux = Flux.fromIterable(animals).log();

        StepVerifier.create(stringFlux)
                .expectNext("dog", "cat", "bird")
                .verifyComplete();
    }
}
