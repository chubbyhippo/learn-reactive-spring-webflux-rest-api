package com.example.demo.controller.v1;

import com.example.demo.document.Item;
import com.example.demo.repository.ItemReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.constants.ItemConstants.ITEM_END_POINT_V1;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public List<Item> data() {
        return Arrays.asList(new Item(null, "Samsung Monitor", 80000.0),
                new Item(null, "LG Monitor", 420.0),
                new Item(null, "Acer Monitor", 1420.99),
                new Item("abc", "Dell Monitor", 1500.0));
    }

    @BeforeEach
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted item is : " + item))
                .blockLast();
    }

    @Test
    public void getAllItemsHasSizeTest() {
        webTestClient.get().uri(ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4);
    }

    @Test
    public void getAllItemsHasSizeConsumeWithTest() {
        webTestClient.get().uri(ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4).consumeWith(listEntityExchangeResult -> {
                    List<Item> responseBody = listEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    responseBody.forEach(item -> assertThat(item.getId()).isNotNull());
                });
    }

    @Test
    public void getAllItemsHasSizeWithStepVerifierTest() {
        Flux<Item> responseBody = webTestClient.get().uri(ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(responseBody.log("value from network : "))
                .expectNextCount(4)
                .verifyComplete();

    }

    @Test
    public void getOneItemTest() {
        webTestClient.get().uri(ITEM_END_POINT_V1.concat("/{id}"), "abc")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 1500.0);
    }

    @Test
    public void getOneItemNotFoundTest() {
        webTestClient.get().uri(ITEM_END_POINT_V1.concat("/{id}"), "abcefg")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItemTest() {
        Item item = new Item(null, "Ipad", 1000.99);
        webTestClient.post().uri(ITEM_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Ipad")
                .jsonPath("$.price").isEqualTo(1000.99);

    }

    @Test
    public void deleteItemTest() {
        webTestClient.delete().uri(ITEM_END_POINT_V1.concat("/{id}"), "abc")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

        StepVerifier.create(itemReactiveRepository.findAll())
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    public void updateItemTest() {
        double newPrice = 9999.99;
        Item item = new Item(null, "Dell Monitor", newPrice);

        webTestClient.put().uri(ITEM_END_POINT_V1.concat("/{id}"), "abc")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo(newPrice);
    }


}