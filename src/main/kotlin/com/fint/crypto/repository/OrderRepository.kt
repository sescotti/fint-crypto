package com.fint.crypto.repository

import com.fint.crypto.domain.Order
import org.springframework.data.repository.CrudRepository
import java.util.*

interface OrderRepository: CrudRepository<Order, UUID> {
}
