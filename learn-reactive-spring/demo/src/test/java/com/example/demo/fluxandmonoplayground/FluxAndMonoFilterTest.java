package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    List<String> animals = Arrays.asList("dog", "cat", "bird");

    @Test
    public void filterTest() {
        Flux<String> stringFlux = Flux.fromIterable(animals)
                .filter(s -> s.startsWith("c"))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("cat")
                .verifyComplete();
    }

    @Test
    public void filterLengthTest() {
        Flux<String> stringFlux = Flux.fromIterable(animals)
                .filter(s -> s.length()> 2)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("dog", "cat", "bird")
                .verifyComplete();
    }
}
