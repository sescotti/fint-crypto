package com.fint.crypto.command

import com.fint.crypto.common.exception.EntityNotFoundException
import com.fint.crypto.common.ICommand
import com.fint.crypto.domain.Order
import com.fint.crypto.domain.Order.Status.CANCELED
import com.fint.crypto.dto.OrderCancellationDto
import com.fint.crypto.repository.OrderRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CancelOrderCommand(private val repository: OrderRepository): ICommand<OrderCancellationDto, Unit> {

    @Transactional
    override fun execute(subject: OrderCancellationDto) {

        val modifiedRecords = repository.updateOrderStatus(subject.orderId, CANCELED)

        if(modifiedRecords == 0){
            throw EntityNotFoundException(Order::class, subject.orderId)
        }

    }

}
