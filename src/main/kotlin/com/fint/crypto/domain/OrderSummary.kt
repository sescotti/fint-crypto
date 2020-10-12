package com.fint.crypto.domain

import org.hibernate.annotations.Subselect
import org.springframework.security.crypto.codec.Hex
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import kotlin.time.Duration

@Subselect(
    """
    SELECT
        type "order_type",
        transaction_amount / SUM(amount_amount) "price_per_coin",
        SUM(amount_amount) "coins_traded",
        GROUP_CONCAT(id) "order_ids"
    FROM "order"
    WHERE status = 'completed'
    GROUP BY type, transaction_amount
    ORDER BY
        CASE type WHEN 'buy' THEN transaction_amount END desc,
        CASE type WHEN 'sell' THEN transaction_amount END asc
    """
)
@Entity
class OrderSummary(
    @Id @Column(name= "orderIds", updatable = false, insertable = false) val id: String,
    @Convert(converter = Order.Type.OrderTypeConverter::class) val orderType: Order.Type,
    val pricePerCoin: BigDecimal,
    val coinsTraded: BigDecimal,
    @Column(insertable = false, updatable = false)
    @Convert(converter = ListConverter::class)
    val orderIds: Set<UUID>

) {

    class ListConverter: AttributeConverter<Set<UUID>, String> {

        override fun convertToDatabaseColumn(attribute: Set<UUID>) = attribute.joinToString(",")

        override fun convertToEntityAttribute(dbData: String) = dbData
            .split(",")
                .asSequence()
                .map { it.trim() }
                .map { Hex.decode(it) }
                .map { UUID.nameUUIDFromBytes(it) }
                .toSet()
    }

}
