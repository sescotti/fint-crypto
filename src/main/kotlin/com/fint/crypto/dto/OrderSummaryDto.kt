package com.fint.crypto.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fint.crypto.common.serializer.BigDecimalSerializer
import com.fint.crypto.domain.Order
import java.math.BigDecimal
import java.util.*

data class OrderSummaryDto(
    val orderType: Order.Type,
    @JsonSerialize(using = BigDecimalSerializer::class) val pricePerCoin: BigDecimal,
    @JsonSerialize(using = BigDecimalSerializer::class)  val coinsTraded: BigDecimal,
    val orderIds: Set<UUID>
)
