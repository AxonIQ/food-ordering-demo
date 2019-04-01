package io.axoniq.foodordering.query

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class FoodCartView(
        @Id val foodCartId: UUID,
        @ElementCollection val products: Map<UUID, Int>
)

interface FoodCartViewRepository : JpaRepository<FoodCartView, UUID>