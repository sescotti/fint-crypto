package com.fint.crypto.domain

import java.math.BigDecimal
import javax.persistence.Embeddable
import javax.persistence.ManyToOne

@Embeddable
data class Money(
    val amount: BigDecimal,
    @ManyToOne val currency: Coin
){

    override fun equals(other: Any?) =
        other is Money &&
        amount.compareTo(other.amount) == 0 &&
        currency.id == other.currency.id

    override fun hashCode(): Int {
        var result = amount.hashCode()
        result = 31 * result + currency.hashCode()
        return result
    }

}
