package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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
}
