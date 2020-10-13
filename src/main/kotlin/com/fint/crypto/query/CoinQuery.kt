package com.fint.crypto.query

import com.fint.crypto.mapper.CoinMapper
import com.fint.crypto.mapper.OrderSummaryMapper
import com.fint.crypto.repository.CoinRepository
import com.fint.crypto.repository.OrderSummaryRepository
import org.springframework.stereotype.Service

@Service
class CoinQuery(
    private val mapper: CoinMapper,
    private val repository: CoinRepository
) {

    fun fetch() = repository.run { findAll() }.map { mapper toCoinDto it }

}
