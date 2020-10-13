package com.fint.crypto.repository

import com.fint.crypto.domain.Coin
import com.fint.crypto.domain.Order
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CoinRepository: CrudRepository<Coin, Long>
