package com.fint.crypto.controller

import com.fint.crypto.query.CoinQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/currencies")
class CoinController(private val coinQuery: CoinQuery) {

    @GetMapping
    fun findAvailableCurrencies() = coinQuery.fetch()

}
