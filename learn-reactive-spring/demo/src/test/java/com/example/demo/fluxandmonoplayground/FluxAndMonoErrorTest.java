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

    @Test
    public void fluxErrorHandlingOnErrorReturnTest() {

        Flux<String> stringFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("d"))
                .onErrorReturn("default");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("a", "b", "c")
                .expectNext("default")
                .verifyComplete();

    }

    @Test
    public void fluxErrorHandlingOnErrorMapTest() {

        Flux<String> stringFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("d"))
                .onErrorMap(CustomException::new);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("a", "b", "c")
                .expectError(CustomException.class)
                .verify();

    }

    private static class CustomException extends Throwable {
        private final String message;

        @Override
        public String getMessage() {
            return message;
        }

        public CustomException(Throwable throwable) {
            this.message = throwable.getMessage();
        }
    }

    @Test
    public void fluxErrorHandlingOnErrorMapWithRetryTest() {

        Flux<String> stringFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("d"))
                .onErrorMap(CustomException::new)
                .retry(2);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("a", "b", "c")
                .expectNext("a", "b", "c")
                .expectNext("a", "b", "c")
                .expectError(CustomException.class)
                .verify();

    }
}
