package com.example.demo.repository;

import com.example.demo.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> items = Arrays.asList(new Item(null, "Samsung Monitor", 800.0),
            new Item(null, "LG Monitor", 420.0),
            new Item(null, "Acer Monitor", 1420.99),
            new Item("abc", "Dell Monitor", 1500.0));


    @BeforeEach
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted item is " + item))
                .blockLast();
    }

    @Test
    public void getAllItemsTest() {
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getItemByIdTest() {
        StepVerifier.create(itemReactiveRepository.findById("abc"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Dell Monitor"))
                .verifyComplete();
    }

    @Test
    public void findItemByDescriptionTest() {
        StepVerifier.create(itemReactiveRepository.findByDescription("Dell Monitor")
                        .log("findItemByDescription : "))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findItemByDescriptionContainsTest() {
        StepVerifier.create(itemReactiveRepository.findByDescriptionContains("Del")
                        .log("findItemByDescriptionContains : "))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findItemByDescriptionStartWithTest() {
        StepVerifier.create(itemReactiveRepository.findByDescriptionEndingWith("tor"))
                .expectNextCount(4)
                .verifyComplete();

    }

    @Test
    public void saveItemTest() {
        var item = new Item("def", "Lenovo Monitor", 3000.0);
        var savedItem = itemReactiveRepository.save(item);
        StepVerifier.create(savedItem)
                .expectNextMatches(item1 -> item1.getDescription().equals("Lenovo Monitor"))
                .verifyComplete();
    }
}