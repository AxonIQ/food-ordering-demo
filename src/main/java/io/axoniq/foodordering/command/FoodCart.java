package io.axoniq.foodordering.command;

import io.axoniq.foodordering.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

@Aggregate
public class FoodCart {

    @AggregateIdentifier
    private UUID foodCartId;
    private Map<UUID, Integer> selectedProducts;

    public FoodCart() {
        // Required by Axon
    }

    @CommandHandler
    public FoodCart(CreateFoodCartCommand command) {
        UUID aggregateId = UUID.randomUUID();
        AggregateLifecycle.apply(new FoodCartCreatedEvent(aggregateId));
    }

    @CommandHandler
    public void handle(SelectProductCommand command) {
        AggregateLifecycle.apply(new ProductSelectedEvent(foodCartId, command.getProductId(), command.getQuantity()));
    }

    @CommandHandler
    public void handle(DeselectProductCommand command) throws ProductDeselectionException {
        if (!selectedProducts.containsKey(command.getProductId())) {
            throw new ProductDeselectionException("...");
        }

        AggregateLifecycle.apply(new ProductDeselectedEvent(foodCartId, command.getProductId(), command.getQuantity()));
    }

    @EventSourcingHandler
    public void on(FoodCartCreatedEvent event) {
        foodCartId = event.getFoodCartId();
        selectedProducts = new HashMap<>();
    }

    @EventSourcingHandler
    public void on(ProductSelectedEvent event) {
        selectedProducts.merge(event.getProductId(), event.getQuantity(), Integer::sum);
    }

}
