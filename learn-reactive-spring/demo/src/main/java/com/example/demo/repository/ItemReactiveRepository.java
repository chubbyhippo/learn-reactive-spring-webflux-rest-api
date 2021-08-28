package com.example.demo.repository;

import com.example.demo.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
    Flux<Item> findByDescription(String description);

    Flux<Item> findByDescriptionContains(String description);

    Flux<Item> findByDescriptionEndingWith(String description);
}
