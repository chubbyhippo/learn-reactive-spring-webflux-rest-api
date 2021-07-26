package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FluxAndMonoFactoryTest {
    List<String> animals = Arrays.asList("dog", "cat", "bird");

    @Test
    public void fluxUsingIterableTest() {
        Flux<String> stringFlux = Flux.fromIterable(animals).log();

        StepVerifier.create(stringFlux)
                .expectNext("dog", "cat", "bird")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArrayTest() {
        String[] animals = new String[]{"dog", "cat", "bird"};
        Flux<String> stringFlux = Flux.fromArray(animals).log();

        StepVerifier.create(stringFlux)
                .expectNext("dog", "cat", "bird")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStreamsTest() {
        Flux<String> stringFlux = Flux.fromStream(animals.parallelStream()).log();

        StepVerifier.create(stringFlux)
                .expectNext("dog", "cat", "bird")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty() {
        Mono<String> stringMono = Mono.justOrEmpty(Optional.empty());
        StepVerifier.create(stringMono)
                .verifyComplete();
    }
}
