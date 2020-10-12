package com.fint.crypto.repository

import com.fint.crypto.domain.OrderSummary
import org.springframework.data.jpa.repository.JpaRepository

interface OrderSummaryRepository: JpaRepository<OrderSummary, String>
