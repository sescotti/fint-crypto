package com.fint.crypto.controller

import com.fint.crypto.common.ICommand
import com.fint.crypto.dto.OrderCancellationDto
import com.fint.crypto.dto.OrderPlacementDto
import com.fint.crypto.query.LiveDashboardQuery
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val placeOrderCommand: ICommand<OrderPlacementDto, UUID>,
    private val cancelOrderCommand: ICommand<OrderCancellationDto, Unit>,
    private val liveDashboardQuery: LiveDashboardQuery
){

    @GetMapping
    fun liveOrdersDashboard() = liveDashboardQuery.run { fetch() }.let { ok(it) }

    @PostMapping
    fun placeOrder(@RequestBody @Validated orderPlacement: OrderPlacementDto) = placeOrderCommand
            .run { execute(orderPlacement) }
            .let { URI("/orders/$it")}
            .let { ResponseEntity.created(it).build<Any>() }

    @DeleteMapping("/{orderId}")
    fun cancelOrder(@PathVariable orderId: UUID) = cancelOrderCommand
        .run { execute(OrderCancellationDto(orderId)) }
        .let { ResponseEntity.noContent().build<Any>() }

}
