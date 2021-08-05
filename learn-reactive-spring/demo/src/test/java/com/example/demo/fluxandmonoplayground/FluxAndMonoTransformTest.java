package com.example.demo.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformTest {
    List<String> animals = Arrays.asList("dog", "cat", "bird");

    @Test
    public void transformUsingMapTest() {
        Flux<String> stringFlux = Flux.fromIterable(animals)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("DOG", "CAT", "BIRD")
                .verifyComplete();

    }

    @Test
    public void transformUsingMapLengthTest() {

        Flux<Integer> integerFlux = Flux.fromIterable(animals)
                .map(String::length)
                .log();

        StepVerifier.create(integerFlux)
                .expectNext(3, 3, 4)
                .verifyComplete();
    }

    @Test
    public void transformUsingMapLengthRepeatTest() {

        Flux<Integer> integerFlux = Flux.fromIterable(animals)
                .map(String::length)
                .repeat(1)
                .log();

        StepVerifier.create(integerFlux)
                .expectNext(3, 3, 4)
                .expectNext(3, 3, 4)
                .verifyComplete();
    }

    @Test
    public void transformUsingFilterMapTest() {

        Flux<String> stringFlux = Flux.fromIterable(animals)
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("BIRD")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMapTest() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D"))
                .flatMap(s -> Flux.fromIterable(convertToList(s)))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(8)
                .verifyComplete();

    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }

    @Test
    public void transformUsingFlatMapParallelTest() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D"))
                .window(2)
                .flatMap(s -> s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(8)
                .verifyComplete();

    }

    @Test
    public void transformUsingFlatMapParallelMaintainOrderTest() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D"))
                .window(2)
                .concatMap(s -> s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("A", "newValue", "B", "newValue", "C", "newValue", "D", "newValue")
                .verifyComplete();

    }
}
