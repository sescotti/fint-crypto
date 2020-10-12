package com.fint.crypto.mapper

import com.fint.crypto.domain.OrderSummary
import com.fint.crypto.dto.OrderSummaryDto
import org.springframework.stereotype.Service

@Service
class OrderSummaryMapper {

    fun toOrderSummaryDto(orderSummary: OrderSummary) = orderSummary.run {
        OrderSummaryDto(
            id = id,
            pricePerCoin = pricePerCoin,
            coinsTraded = coinsTraded,
            orderIds = orderIds,
            orderType = orderType
        )
    }
}
