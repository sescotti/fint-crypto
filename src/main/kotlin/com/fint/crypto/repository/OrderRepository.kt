package com.fint.crypto.repository

import com.fint.crypto.domain.Order
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface OrderRepository: CrudRepository<Order, UUID> {

    @Modifying
    @Query("UPDATE Order SET status = :status WHERE id = :id")
    fun updateOrderStatus(id: UUID, status: Order.Status): Int

}
