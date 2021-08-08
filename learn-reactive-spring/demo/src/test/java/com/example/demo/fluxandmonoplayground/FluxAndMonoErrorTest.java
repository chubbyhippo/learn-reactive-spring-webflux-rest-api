package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandlingOnErrorResumeTest() {

        Flux<String> stringFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("d"))
                .onErrorResume(throwable -> {
                    System.out.println("Exception is : " + throwable);
                    return Flux.just("default", "default1");
                });


        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("a", "b", "c")
//                .expectError(RuntimeException.class)
//                .verify();
                .expectNext("default", "default1")
                .verifyComplete();


    }
}
