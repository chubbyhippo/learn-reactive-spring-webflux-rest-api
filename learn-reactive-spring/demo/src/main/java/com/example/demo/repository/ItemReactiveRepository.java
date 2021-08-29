package com.example.demo.repository;

import com.example.demo.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
    Mono<Item> findByDescription(String description);

    Flux<Item> findByDescriptionContains(String description);

    Flux<Item> findByDescriptionEndingWith(String description);
}
