package com.fint.crypto.mapper

import com.fint.crypto.domain.Order.Status.CREATED
import com.fint.crypto.domain.Order.Type.BUY
import com.fint.crypto.dto.OrderPlacementDto
import com.fint.crypto.dto.OrderPlacementDto.CoinDto
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OrderMapperTest {

    private val mapper = OrderMapper()

    @Test
    fun `verify that all values are correctly mapped when placing a new order`() {

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

        // WHEN
        val order = mapper toOrder dto

        // THEN
        order.apply {

            assertSoftly { softly ->
                softly.assertThat(id).isNotNull()
                softly.assertThat(type).isEqualTo(dto.type)

                softly.assertThat(amount.amount.compareTo(dto.coin.quantity)).isZero
                softly.assertThat(amount.currency.id).isEqualTo(dto.coin.currencyId)

                softly.assertThat(transaction.amount.compareTo(dto.coin.quantity * dto.pricePerCoin)).isZero
                softly.assertThat(transaction.currency.id).isEqualTo(4L)

                softly.assertThat(status).isEqualTo(CREATED)
                softly.assertThat(pricePerCoin.compareTo(dto.pricePerCoin)).isZero
            }

        }

    }

}
