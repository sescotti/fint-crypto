package com.fint.crypto.command

import com.fint.crypto.domain.Coin
import com.fint.crypto.domain.Money
import com.fint.crypto.domain.Order
import com.fint.crypto.domain.Order.Type.BUY
import com.fint.crypto.dto.OrderPlacementDto
import com.fint.crypto.dto.OrderPlacementDto.*
import com.fint.crypto.mapper.OrderMapper
import com.fint.crypto.repository.OrderRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class PlaceOrderCommandTest {

    private val repository = mock<OrderRepository>()
    private val mapper = mock<OrderMapper>()
    private val command = PlaceOrderCommand(mapper, repository)

    @BeforeEach
    fun setup(){
        reset(mapper, repository)
    }

    @Test
    fun `command should save an order with the info of the received dto and return its id`() {

        // GIVEN
        val dto = OrderPlacementDto(
            userId = 1L,
            type = BUY,
            coin = CoinDto(
                quantity = BigDecimal("10"),
                currencyId = 1L
            ),
            pricePerCoin = BigDecimal("50000")
        )

        val order = dto.run {
            Order(
                userId = userId,
                type = type,
                amount = coin.run { Money(
                    amount = coin.quantity,
                    currency = Coin(coin.currencyId)
                )},
                transaction = Money(
                    amount = BigDecimal("500000"),
                    currency = Coin(4L)
                ),
                id = UUID.randomUUID()
            )
        }

        whenever(mapper.toOrder(dto)).thenReturn(order)
        whenever(repository.save(order)).thenReturn(order)

        // WHEN
        val id = command.execute(dto)

        // THEN
        assertThat(id).isEqualTo(order.id)

    }
}