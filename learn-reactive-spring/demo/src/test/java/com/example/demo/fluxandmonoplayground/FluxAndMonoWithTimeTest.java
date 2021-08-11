package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(200)).log();

        interval.subscribe(aLong -> System.out.println("Value is : " + aLong));

        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequenceTest() {

        Flux<Long> interval = Flux.interval(Duration.ofMillis(200))
                .take(3)
                .log();

        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0L, 1L ,2L)
                .verifyComplete();

    }

    @Test
    public void infiniteSequenceMapTest() {

        Flux<Integer> interval = Flux.interval(Duration.ofMillis(200))
                .map(Long::intValue)
                .take(3)
                .log();

        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0, 1 ,2)
                .verifyComplete();

    }

    @Test
    public void infiniteSequenceMapWithDelayTest() {

        Flux<Integer> interval = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofSeconds(1))
                .map(Long::intValue)
                .take(3)
                .log();

        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0, 1 ,2)
                .verifyComplete();

    }
}
