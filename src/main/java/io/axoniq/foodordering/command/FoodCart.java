package io.axoniq.foodordering.command;

import io.axoniq.foodordering.coreapi.ConfirmOrderCommand;
import io.axoniq.foodordering.coreapi.CreateFoodCartCommand;
import io.axoniq.foodordering.coreapi.DeselectProductCommand;
import io.axoniq.foodordering.coreapi.FoodCartCreatedEvent;
import io.axoniq.foodordering.coreapi.OrderConfirmedEvent;
import io.axoniq.foodordering.coreapi.ProductDeselectedEvent;
import io.axoniq.foodordering.coreapi.ProductDeselectionException;
import io.axoniq.foodordering.coreapi.ProductSelectedEvent;
import io.axoniq.foodordering.coreapi.SelectProductCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aggregate
class FoodCart {

    private static final Logger logger = LoggerFactory.getLogger(FoodCart.class);

    @AggregateIdentifier
    private UUID foodCartId;
    private Map<UUID, Integer> selectedProducts;
    private boolean confirmed;

    public FoodCart() {
        // Required by Axon
    }

    @CommandHandler
    public FoodCart(CreateFoodCartCommand command) {
        AggregateLifecycle.apply(new FoodCartCreatedEvent(command.getFoodCartId()));
    }

    @CommandHandler
    public void handle(SelectProductCommand command) {
        AggregateLifecycle.apply(new ProductSelectedEvent(foodCartId, command.getProductId(), command.getQuantity()));
    }

    @CommandHandler
    public void handle(DeselectProductCommand command) throws ProductDeselectionException {
        UUID productId = command.getProductId();
        int quantity = command.getQuantity();

        if (!selectedProducts.containsKey(productId)) {
            throw new ProductDeselectionException(
                    "Cannot deselect a product which has not been selected for this Food Cart"
            );
        }
        if (selectedProducts.get(productId) - quantity < 0) {
            throw new ProductDeselectionException(
                    "Cannot deselect more products of ID [" + productId + "] than have been selected initially"
            );
        }

        AggregateLifecycle.apply(new ProductDeselectedEvent(foodCartId, productId, quantity));
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command) {
        if (confirmed) {
            logger.warn("Cannot confirm a Food Cart order which is already confirmed");
            return;
        }

        AggregateLifecycle.apply(new OrderConfirmedEvent(foodCartId));
    }

    @EventSourcingHandler
    public void on(FoodCartCreatedEvent event) {
        foodCartId = event.getFoodCartId();
        selectedProducts = new HashMap<>();
        confirmed = false;
    }

    @EventSourcingHandler
    public void on(ProductSelectedEvent event) {
        selectedProducts.merge(event.getProductId(), event.getQuantity(), Integer::sum);
    }

    @EventSourcingHandler
    public void on(ProductDeselectedEvent event) {
        selectedProducts.computeIfPresent(
                event.getProductId(),
                (productId, quantity) -> quantity -= event.getQuantity()
        );
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        confirmed = true;
    }
}
