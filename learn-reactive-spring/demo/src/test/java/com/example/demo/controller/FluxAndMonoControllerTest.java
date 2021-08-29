package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest
@DirtiesContext
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxTest() {
        Flux<Integer> responseBody = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void fluxSizeTest() {

        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    public void fluxEntityExchangeTEst() {

        List<Integer> expectedIntegerList = Arrays.asList(1, 2, 3, 4);

        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        assertThat(expectedIntegerList).isEqualTo(listEntityExchangeResult.getResponseBody());
    }

    @Test
    public void fluxConsumeWithTest() {

        List<Integer> expectedIntegerList = Arrays.asList(1, 2, 3, 4);

        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    assertThat(expectedIntegerList).isEqualTo(listEntityExchangeResult.getResponseBody());
                });
    }

    @Test
    public void fluxStreamTest() {
        Flux<Long> longFlux = webTestClient.get().uri("/fluxstream")
                .accept(MediaType.valueOf(MediaType.APPLICATION_NDJSON_VALUE))
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .thenCancel()
                .verify();

    }

    @Test
    public void monoTest() {
        Integer expectedValue = 1;

        webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(integerEntityExchangeResult -> assertThat(expectedValue)
                        .isEqualTo(integerEntityExchangeResult.getResponseBody()));
    }

}