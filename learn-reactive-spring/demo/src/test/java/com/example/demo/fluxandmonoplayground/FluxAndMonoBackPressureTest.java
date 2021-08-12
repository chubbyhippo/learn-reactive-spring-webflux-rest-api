package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> range = Flux.range(1, 10);

        StepVerifier.create(range)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(2)
                .expectNext(2, 3)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {
        Flux<Integer> integerFlux = Flux.range(1, 10)
                .log();
// deprecated
//        integerFlux.subscribe(integer -> System.out.println("Element is : " + integer)
//                , throwable -> System.out.println("Exception is : " + throwable)
//                , () -> System.out.println("Done")
//                , subscription -> subscription.request(2));

        integerFlux.subscribe(new Subscriber<>() {

            @Override
            public void onSubscribe(Subscription s) {
                s.request(2);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("Element is : " + integer);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Exception is : " + t);
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        });


    }
}
