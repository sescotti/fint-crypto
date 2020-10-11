package com.fint.crypto.domain

import com.fint.crypto.domain.Order.Type.BUY
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

internal class OrderTest {

    @Test
    fun `price per coin should represent the price for each coin bought or sold`() {

        // GIVEN
        val order = Order(
            id = UUID.randomUUID(),
            userId = 1L,
            amount = Money(
                amount = BigDecimal(10),
                currency = Coin(1)
            ),
            transaction = Money(
                amount = BigDecimal(8500),
                currency = Coin(4)
            ),
            type = BUY
        )

        // WHEN
        val pricePerCoin = order.pricePerCoin

        // THEN
        assertThat(pricePerCoin.compareTo(BigDecimal(850))).isZero()

    }

    @Test
    fun `price per coin should represent the price truncated to the order's precision`() {

        // GIVEN
        val order = Order(
            id = UUID.randomUUID(),
            userId = 1L,
            amount = Money(
                amount = BigDecimal(8500),
                currency = Coin(1)
            ),
            transaction = Money(
                amount = BigDecimal(10),
                currency = Coin(4)
            ),
            type = BUY
        )

        // WHEN
        val pricePerCoin = order.pricePerCoin

        // THEN
        assertThat(pricePerCoin).isEqualTo(BigDecimal("0.0011764705"))

    }

}