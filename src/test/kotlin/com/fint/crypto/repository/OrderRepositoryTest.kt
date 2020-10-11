package com.fint.crypto.repository

import com.fint.crypto.domain.Coin
import com.fint.crypto.domain.Money
import com.fint.crypto.domain.Order
import com.fint.crypto.domain.Order.Status.CREATED
import com.fint.crypto.domain.Order.Type.BUY
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.util.*
import javax.transaction.Transactional
import javax.transaction.Transactional.TxType.NOT_SUPPORTED

@ExtendWith(SpringExtension::class)
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private lateinit var repository: OrderRepository

    @Test
    @Transactional(NOT_SUPPORTED) // Disabling transaction to test that effectively the amounts are correctly persisted in the DB
    fun `amounts precision should be kept at a database level and by default order should have created status`(){

        // GIVEN
        val order = Order(
            id = UUID.randomUUID(),
            userId = 1L,
            amount = Money(
                amount = BigDecimal("1000000000.0000000001"),
                currency = Coin(1)
            ),
            transaction = Money(
                amount = BigDecimal("9999999999.9999999999"),
                currency = Coin(4)
            ),
            type = BUY
        )

        // WHEN
        repository.save(order)

        val savedOrder = repository.findById(order.id).get()

        // THEN
        savedOrder.apply {
            assertSoftly { softly ->
                softly.assertThat(id).isEqualTo(order.id)
                softly.assertThat(userId).isEqualTo(order.userId)
                softly.assertThat(amount).isEqualTo(order.amount)
                softly.assertThat(transaction).isEqualTo(order.transaction)
                softly.assertThat(type).isEqualTo(order.type)
                softly.assertThat(status).isEqualTo(CREATED)
            }
        }

    }

    @Test
    @Transactional(NOT_SUPPORTED) // Disabling transaction to test that effectively the amounts are correctly persisted in the DB
    fun `amounts with precision higher than supported will not fail because they'll be truncated`(){

        // GIVEN
        val order = Order(
            id = UUID.randomUUID(),
            userId = 1L,
            amount = Money(
                amount = BigDecimal("1.012345678901234567890000000000000000001"),
                currency = Coin(1)
            ),
            transaction = Money(
                amount = BigDecimal("1.1"),
                currency = Coin(4)
            ),
            type = BUY
        )

        // WHEN
        repository.save(order)

        // THEN
        val savedOrder = repository.findById(order.id).get()

        savedOrder.apply {
            assertSoftly { softly ->
                softly.assertThat(id).isEqualTo(order.id)
                softly.assertThat(userId).isEqualTo(order.userId)
                softly.assertThat(amount).isEqualTo(Money(
                    amount = BigDecimal("1.0123456789"),
                    currency = Coin(order.amount.currency.id)
                ))
                softly.assertThat(transaction).isEqualTo(order.transaction)
                softly.assertThat(type).isEqualTo(order.type)
                softly.assertThat(status).isEqualTo(CREATED)
            }
        }

    }

    @Test
    @Transactional(NOT_SUPPORTED) // Disabling transaction to test that effectively the amounts are correctly persisted in the DB
    fun `amounts with scale higher than supported will make the save method to fail`(){

        // GIVEN
        val order = Order(
            id = UUID.randomUUID(),
            userId = 1L,
            amount = Money(
                amount = BigDecimal("10000000000.1"),
                currency = Coin(1)
            ),
            transaction = Money(
                amount = BigDecimal("1.1"),
                currency = Coin(4)
            ),
            type = BUY
        )

        // WHEN
        val exception = assertThrows<DataIntegrityViolationException> { repository.save(order) }

        // THEN
        assertThat(exception.mostSpecificCause.message).contains("Value too long for column")

    }

}
