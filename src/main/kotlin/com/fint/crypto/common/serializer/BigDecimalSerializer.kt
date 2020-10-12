package com.fint.crypto.common.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fint.crypto.domain.Order
import java.math.BigDecimal
import java.math.RoundingMode

class BigDecimalSerializer: JsonSerializer<BigDecimal>() {

    override fun serialize(value: BigDecimal, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeNumber(value.setScale(Order.SCALE, RoundingMode.HALF_EVEN))
    }

}
