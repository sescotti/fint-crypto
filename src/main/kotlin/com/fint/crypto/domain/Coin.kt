package com.fint.crypto.domain

import com.fint.crypto.domain.Coin.Type.CRYPTO
import com.fint.crypto.domain.Coin.Type.CoinTypeConverter
import javax.persistence.AttributeConverter
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Coin(
    @Id
    val id: Long,
    val symbol: String = "",
    val name: String = "",
    @Convert(converter = CoinTypeConverter::class) val type: Type = CRYPTO
){

    enum class Type {
        CRYPTO, FIAT;

        class CoinTypeConverter : AttributeConverter<Type, String> {
            override fun convertToDatabaseColumn(type: Type) = type.toString()
            override fun convertToEntityAttribute(type: String) = valueOf(type.toUpperCase())
        }

    }

}
