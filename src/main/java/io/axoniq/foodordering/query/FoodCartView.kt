package io.axoniq.foodordering.query

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id

@Entity
data class FoodCartView(
        @Id val foodCartId: UUID,
        @ElementCollection(fetch = FetchType.EAGER) val products: MutableMap<UUID, Int>
) {

    /**
     * Adds the given [productId] with the given [amount] to the map of [products].
     * Does this by performing [MutableMap.compute], where the second parameter is a bi-function.
     * The [productId] is not important for the bi-function, hence it's replaced by `_`.
     * Lastly, the `quantity` field in the bi-function is nullable, thus explaining why the elvis operator is in place.
     */
    fun addProducts(productId: UUID, amount: Int) =
            products.compute(productId) { _, quantity -> (quantity ?: 0) + amount }

    /**
     * Removes the specified [amount] of the given [productId] from the [products] map.
     * Does this by performing [MutableMap.compute], where the second parameter is a bi-function.
     * The [productId] is not important for the bi-function, hence it's replaced by `_`.
     * Lastly, the `quantity` field in the bi-function is nullable, thus explaining why the elvis operator is in place.
     *
     * If the left over quantity is zero, the product will be completely removed from the map.
     */
    fun removeProducts(productId: UUID, amount: Int) {
        val leftOverQuantity = products.compute(productId) { _, quantity -> (quantity ?: 0) - amount }
        if (leftOverQuantity == 0) {
            products.remove(productId)
        }
    }
}


interface FoodCartViewRepository : JpaRepository<FoodCartView, UUID>