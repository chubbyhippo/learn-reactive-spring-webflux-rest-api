package com.example.demo.controller.v1;

import com.example.demo.document.Item;
import com.example.demo.repository.ItemReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.example.demo.constants.ItemConstants.ITEM_END_POINT_V1;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_END_POINT_V1)
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }


}
