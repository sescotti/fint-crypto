package com.fint.crypto.query

import com.fint.crypto.mapper.OrderSummaryMapper
import com.fint.crypto.repository.OrderSummaryRepository
import org.springframework.stereotype.Service

@Service
class LiveDashboardQuery(
    private val mapper: OrderSummaryMapper,
    private val repository: OrderSummaryRepository
) {

    fun fetch() = repository.run { findAll() }.map { mapper.toOrderSummaryDto(it) }

}
