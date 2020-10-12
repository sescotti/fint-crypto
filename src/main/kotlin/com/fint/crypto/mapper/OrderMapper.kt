package com.fint.crypto.mapper

import com.fint.crypto.domain.Coin
import com.fint.crypto.domain.Money
import com.fint.crypto.domain.Order
import com.fint.crypto.dto.OrderPlacementDto
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderMapper {

    infix fun toOrder(dto: OrderPlacementDto) = dto.run {
        Order(
            id = UUID.randomUUID(),
            amount = coin.run { Money(quantity, currency = Coin(currencyId)) },
            type = type,
            userId = userId,
            transaction = Money(amount = pricePerCoin * coin.quantity, currency = Coin(DEFAULT_COIN_ID))
        )
    }

    companion object {
        private const val DEFAULT_COIN_ID = 4L
    }

}
