package com.fint.crypto.mapper

import com.fint.crypto.domain.Coin
import com.fint.crypto.domain.Money
import com.fint.crypto.domain.Order
import com.fint.crypto.dto.CoinDto
import org.springframework.stereotype.Service
import java.util.*

@Service
class CoinMapper {

    infix fun toCoinDto(coin: Coin) = coin.run {
        CoinDto(
            id = id,
            type = type,
            name = name,
            symbol = symbol
        )
    }

    companion object {
        private const val DEFAULT_COIN_ID = 4L
    }

}
