package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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
    public void fluxUsingRangeTest() {
        Flux<Integer> integerFlux = Flux.range(1, 5);

        StepVerifier.create(integerFlux)
                .expectNext(1,2,3,4,5)
                .verifyComplete();

    }

    @Test
    public void monoUsingJustOrEmpty() {
        Mono<String> stringMono = Mono.justOrEmpty(Optional.empty());
        StepVerifier.create(stringMono)
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplierTest() {
        Supplier<String> stringSupplier = () -> "dog";

        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(stringMono.log())
                .expectNext("dog")
                .verifyComplete();
    }
}
