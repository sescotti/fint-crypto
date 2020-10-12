package com.fint.crypto.controller

import com.fint.crypto.EntityNotFoundException
import com.fint.crypto.command.CancelOrderCommand
import com.fint.crypto.command.PlaceOrderCommand
import com.fint.crypto.domain.Order
import com.fint.crypto.domain.Order.Type.BUY
import com.fint.crypto.dto.OrderCancellationDto
import com.fint.crypto.dto.OrderPlacementDto
import com.fint.crypto.dto.OrderPlacementDto.CoinDto
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal
import java.util.*

@WebMvcTest(OrderController::class)
@ExtendWith(SpringExtension::class)
class OrderControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var placeOrderCommand: PlaceOrderCommand

    @MockBean
    private lateinit var cancelOrderCommand: CancelOrderCommand

    @Nested
    inner class OrderPlacementCases {

        @Test
        fun `when placing order response should contain a header with order id and no body with status 201`() {

            // GIVEN
            val orderId = UUID.randomUUID()
            val requestBody = """
            {
                "user_id": 1,
                "coin": {
                    "currency_id": 1,
                    "quantity": "1.0000"
                },
                "price_per_coin": "10",
                "type": "buy"
            }
        """.trimIndent()

            val orderPlacement = OrderPlacementDto(
                userId = 1L,
                coin = CoinDto(quantity = BigDecimal(1.0000), currencyId = 1L),
                type = BUY,
                pricePerCoin = BigDecimal(10)
            )

            whenever(placeOrderCommand.execute(orderPlacement)).thenReturn(orderId)

            // WHEN
            mockMvc.perform(
                post("/orders")
                    .contentType(APPLICATION_JSON)
                    .content(requestBody)
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(header().string("Location", "/orders/$orderId"))

        }

        @Test
        fun `when placing an empty order placement request body response should throw a 400 error`() {

            // GIVEN
            val orderId = UUID.randomUUID()
            val requestBody = """
            {
            }
        """.trimIndent()

            val orderPlacement = OrderPlacementDto(
                userId = 1L,
                coin = CoinDto(quantity = BigDecimal(1.0000), currencyId = 1L),
                type = BUY,
                pricePerCoin = BigDecimal(10)
            )

            whenever(placeOrderCommand.execute(orderPlacement)).thenReturn(orderId)

            // WHEN
            mockMvc.perform(
                post("/orders").contentType(APPLICATION_JSON).content(requestBody)
            )
                .andExpect(status().isBadRequest)

        }

    }

    @Nested
    inner class OrderCancellationCases {

        @Test
        fun `when cancelling an order response, should return a 204 status with no body`() {

            // GIVEN
            val orderId = UUID.randomUUID()

            // WHEN
            mockMvc
                .perform(delete("/orders/${orderId}").contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent)
                .andExpect(jsonPath("$").doesNotExist())

        }

        @Test
        fun `when cancelling an order that doesn't exist, response should return a 404`() {

            // GIVEN
            val orderId = UUID.randomUUID()

            whenever(cancelOrderCommand.execute(OrderCancellationDto(orderId)))
                .thenThrow(EntityNotFoundException(Order::class, orderId))

            // WHEN
            mockMvc
                .perform(delete("/orders/${orderId}").contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound)

        }

    }

}
