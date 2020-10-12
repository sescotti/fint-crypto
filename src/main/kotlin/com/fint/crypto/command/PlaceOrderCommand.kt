package com.fint.crypto.command

import com.fint.crypto.common.ICommand
import com.fint.crypto.dto.OrderPlacementDto
import com.fint.crypto.mapper.OrderMapper
import com.fint.crypto.repository.OrderRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlaceOrderCommand(
    private val mapper: OrderMapper,
    private val orderRepository: OrderRepository
): ICommand<OrderPlacementDto, UUID> {

    override fun execute(subject: OrderPlacementDto) = mapper
            .run { toOrder(subject) }
            .let { orderRepository.save(it) }
            .run { id }

}
