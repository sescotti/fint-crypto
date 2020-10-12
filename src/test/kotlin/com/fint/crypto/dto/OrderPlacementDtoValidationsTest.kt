package com.fint.crypto.dto

import com.fint.crypto.domain.Order.Type.BUY
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import javax.validation.Validation


class OrderPlacementDtoValidationsTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `a valid dto should not raise any error`(){

        // GIVEN
        val orderPlacement = OrderPlacementDto(
            userId = 1L,
            coin = OrderPlacementDto.CoinDto(quantity = BigDecimal(1.0000), currencyId = 1L),
            type = BUY,
            pricePerCoin = BigDecimal(10)
        )

        // WHEN
        val validationErrors = validator.validate(orderPlacement)

        // THEN
        assertThat(validationErrors).isEmpty()

    }

    @Test
    fun `a dto with a negative coin quantity should raise an error`(){

        // GIVEN
        val orderPlacement = OrderPlacementDto(
            userId = -1L,
            coin = OrderPlacementDto.CoinDto(quantity = BigDecimal(-1.0000), currencyId = 0L),
            type = BUY,
            pricePerCoin = BigDecimal(-10)
        )

        // WHEN
        val validationErrors = validator.validate(orderPlacement)

        // THEN
        assertThat(validationErrors).size().isEqualTo(4)
        assertThat(validationErrors).anySatisfy{ t -> assertThat(t.propertyPath.toString()).isEqualTo("userId") }
        assertThat(validationErrors).anySatisfy{ t -> assertThat(t.propertyPath.toString()).isEqualTo("coin.quantity") }
        assertThat(validationErrors).anySatisfy{ t -> assertThat(t.propertyPath.toString()).isEqualTo("coin.currencyId") }
        assertThat(validationErrors).anySatisfy{ t -> assertThat(t.propertyPath.toString()).isEqualTo("pricePerCoin") }

    }

}
