package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {
    @Test
    public void coldPublisher() throws InterruptedException {
        Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4, 5, 6, 7).delayElements(Duration.ofSeconds(1));

        integerFlux.subscribe(integer -> System.out.println("Subscriber 1 : " + integer));
        Thread.sleep(2000);
        integerFlux.subscribe(integer -> System.out.println("Subscriber 2 : " + integer));
        Thread.sleep(4000);
    }
}
