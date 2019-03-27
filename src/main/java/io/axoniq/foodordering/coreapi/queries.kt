package io.axoniq.foodordering.coreapi

import java.util.*

data class FindFoodCartQuery(val foodCartId: UUID)

class RetrieveProductOptionsQuery