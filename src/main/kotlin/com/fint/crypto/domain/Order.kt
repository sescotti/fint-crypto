package com.fint.crypto.domain

import com.fint.crypto.domain.Order.Status.CREATED
import com.fint.crypto.domain.Order.Status.OrderStatusConverter
import com.fint.crypto.domain.Order.Type.OrderTypeConverter
import java.math.BigDecimal
import java.math.RoundingMode.FLOOR
import java.math.RoundingMode.HALF_EVEN
import java.util.*
import javax.persistence.*

/**
 * Order domain entity
 *
 * Maximum precision for purchases (Price: 10dec | Vol: 8dec)
 * https://support.kraken.com/hc/en-us/articles/360001389366-Price-and-volume-decimal-precision
 *
 */
@Entity
data class Order(

    @Id val id: UUID,

    @Column(nullable = false, updatable = false) val userId: Long,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(
            name = "amount",
            column = Column(name="amount_amount", nullable = false, updatable = false, precision = PRECISION, scale = SCALE)
        )
    )
    @AssociationOverrides(
        AssociationOverride(
            name = "currency",
            joinColumns = [
                JoinColumn(name = "amount_coin_id", nullable = false, updatable = false)
            ]
        )
    )
    val amount: Money, // Find better naming

    @AttributeOverrides(
        AttributeOverride(
            name = "amount",
            column = Column(name="transaction_amount", nullable = false, updatable = false, precision = PRECISION, scale = SCALE)
        )
    )
    @AssociationOverrides(
        AssociationOverride(
            name = "currency",
            joinColumns = [
                JoinColumn(name = "transaction_coin_id", nullable = false, updatable = false)
            ]
        )
    )
    @Embedded
    val transaction: Money,

    @Column(nullable = false, updatable = false)
    @Convert(converter = OrderTypeConverter::class) val type: Type,

    @Column(nullable = false)
    @Convert(converter = OrderStatusConverter::class) var status: Status = CREATED

){

    @get:Transient
    @delegate:Transient
    val pricePerCoin: BigDecimal by lazy {
        transaction.amount.divide(amount.amount, SCALE, FLOOR)
    }


    enum class Type {

        BUY, SELL;

        override fun toString() = name.toLowerCase()

        class OrderTypeConverter : AttributeConverter<Type, String> {
            override fun convertToDatabaseColumn(type: Type) = type.toString()
            override fun convertToEntityAttribute(type: String) = valueOf(type.toUpperCase())
        }

    }

    enum class Status {

        CREATED, COMPLETED, CANCELED;

        override fun toString() = name.toLowerCase()

        class OrderStatusConverter : AttributeConverter<Status, String> {
            override fun convertToDatabaseColumn(status: Status) = status.toString()
            override fun convertToEntityAttribute(status: String) = valueOf(status.toUpperCase())
        }

    }

    companion object {
        private const val PRECISION = 20
        private const val SCALE = 10
    }

}
