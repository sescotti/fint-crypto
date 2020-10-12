package com.fint.crypto.dto

import com.fint.crypto.domain.Order
import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class OrderPlacementDto(
    @field:NotNull @field:Positive val userId: Long,
    @field:NotNull @field:Valid val coin: CoinDto,
    @field:NotNull val type: Order.Type,
    @field:NotNull @field:Positive @field:Digits(integer = 10, fraction = 10) val pricePerCoin: BigDecimal
) {

    override fun equals(other: Any?) =
        other is OrderPlacementDto &&
        userId == other.userId &&
        coin == other.coin &&
        type == other.type &&
        pricePerCoin.compareTo(pricePerCoin) == 0

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + coin.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + pricePerCoin.hashCode()
        return result
    }


    data class CoinDto(
        @field:Positive @field:Digits(integer = 10, fraction = 10) val quantity: BigDecimal,
        @field:NotNull @field:Positive val currencyId: Long
    ) {

        override fun equals(other: Any?) =
            other is CoinDto &&
            quantity.compareTo(other.quantity) == 0 &&
            currencyId == currencyId

        override fun hashCode(): Int {
            var result = quantity.hashCode()
            result = 31 * result + currencyId.hashCode()
            return result
        }

    }

}
