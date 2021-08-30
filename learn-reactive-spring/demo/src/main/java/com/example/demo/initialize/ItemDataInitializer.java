package com.example.demo.initialize;

import com.example.demo.document.Item;
import com.example.demo.repository.ItemReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemDataInitializer implements CommandLineRunner {

    private final ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) {
        init();
    }

    public List<Item> data() {
        return Arrays.asList(new Item(null, "Samsung Monitor", 800.0),
                new Item(null, "LG Monitor", 420.0),
                new Item(null, "Acer Monitor", 1420.99),
                new Item("abc", "Dell Monitor", 1500.0));
    }

    private void init() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> System.out.println("Item inserted " + item));
    }
}
