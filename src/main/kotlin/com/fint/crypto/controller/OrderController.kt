package com.fint.crypto.controller

import com.fint.crypto.common.ICommand
import com.fint.crypto.dto.OrderPlacementDto
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val placeOrderCommand: ICommand<OrderPlacementDto, UUID>
){

    @PostMapping
    fun placeOrder(@RequestBody @Validated orderPlacement: OrderPlacementDto) = placeOrderCommand
            .run { execute(orderPlacement) }
            .let { URI("/orders/$it")}
            .let { ResponseEntity.created(it).build<Any>() }

}
