package com.fint.crypto.command

import com.fint.crypto.EntityNotFoundException
import com.fint.crypto.domain.Order
import com.fint.crypto.domain.Order.Status.CANCELED
import com.fint.crypto.dto.OrderCancellationDto
import com.fint.crypto.repository.OrderRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CancelOrderCommandTest {

    private val repository = mock<OrderRepository>()
    private val command = CancelOrderCommand(repository)

    @Test
    fun `command should update order status to cancelled`() {

        // GIVEN
        val orderId = UUID.randomUUID()
        val orderCancellation = OrderCancellationDto(orderId = orderId)
        whenever(repository.updateOrderStatus(orderId, CANCELED)).thenReturn(1)

        // WHEN
        command.execute(orderCancellation)

        // THEN
        verify(repository).updateOrderStatus(orderId, CANCELED)

    }

    @Test
    fun `command should throw an entity not found exception if no order was cancelled`() {

        // GIVEN
        val orderId = UUID.randomUUID()
        val orderCancellation = OrderCancellationDto(orderId = orderId)
        whenever(repository.updateOrderStatus(orderId, CANCELED)).thenReturn(0)

        // WHEN
        val exception = assertThrows<EntityNotFoundException> { command.execute(orderCancellation) }

        // THEN
        exception.apply {
            assertThat(entityClass).isEqualTo(Order::class)
            assertThat(parameters).isEqualTo(mapOf("id" to orderId))
        }

    }

}
