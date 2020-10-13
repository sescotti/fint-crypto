package com.fint.crypto.dto

import com.fint.crypto.domain.Coin

data class CoinDto(
    val id: Long,
    val symbol: String,
    val name: String,
    val type: Coin.Type
)
