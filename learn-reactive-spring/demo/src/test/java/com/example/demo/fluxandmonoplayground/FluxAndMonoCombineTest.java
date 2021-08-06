package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMergeTest() {
        Flux<String> firstFlux = Flux.just("a", "b", "c");
        Flux<String> secondFlux = Flux.just("d", "e", "f");

        Flux<String> mergedFlux = Flux.merge(firstFlux, secondFlux);

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNext("a", "b", "c", "d", "e", "f")
                .verifyComplete();


    }

    @Test
    public void combineUsingMergeWithDelayTest() {
        Flux<String> firstFlux = Flux.just("a", "b", "c").delayElements(Duration.ofSeconds(1));
        Flux<String> secondFlux = Flux.just("d", "e", "f").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.merge(firstFlux, secondFlux);

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
//                .expectNext("a", "b", "c", "d", "e", "f")
                .expectNextCount(6)
                .verifyComplete();


    }

    @Test
    public void combineUsingConcatTest() {
        Flux<String> firstFlux = Flux.just("a", "b", "c");
        Flux<String> secondFlux = Flux.just("d", "e", "f");

        Flux<String> mergedFlux = Flux.concat(firstFlux, secondFlux);

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNext("a", "b", "c", "d", "e", "f")
                .verifyComplete();


    }

    @Test
    public void combineUsingConcatWithDelayTest() {
        Flux<String> firstFlux = Flux.just("a", "b", "c").delayElements(Duration.ofSeconds(1));
        Flux<String> secondFlux = Flux.just("d", "e", "f").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.concat(firstFlux, secondFlux);

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNext("a", "b", "c", "d", "e", "f")
                .verifyComplete();


    }
}
