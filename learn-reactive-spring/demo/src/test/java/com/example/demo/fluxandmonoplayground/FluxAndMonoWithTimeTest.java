package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(200)).log();

        interval.subscribe(aLong -> System.out.println("Value is : " + aLong));

        Thread.sleep(3000);
    }
}
