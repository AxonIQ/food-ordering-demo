package io.axoniq.foodordering.query;

import io.axoniq.foodordering.coreapi.FindFoodCartQuery;
import io.axoniq.foodordering.coreapi.FoodCartCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class FoodCartEventHandler {

    private final FoodCartViewRepository foodCartViewRepository;

    public FoodCartEventHandler(FoodCartViewRepository foodCartViewRepository) {
        this.foodCartViewRepository = foodCartViewRepository;
    }

    @EventHandler
    public void on(FoodCartCreatedEvent event) {
        FoodCartView foodCartView = new FoodCartView(event.getFoodCartId(), Collections.emptyMap());
        foodCartViewRepository.save(foodCartView);
    }

    @QueryHandler
    public FoodCartView handle(FindFoodCartQuery query) {
        return foodCartViewRepository.findById(query.getFoodCartId()).orElse(null);
    }

}
