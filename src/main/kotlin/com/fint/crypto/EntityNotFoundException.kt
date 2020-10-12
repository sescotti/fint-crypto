package com.fint.crypto

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException
import java.util.*
import kotlin.reflect.KClass

@ResponseStatus(NOT_FOUND)
class EntityNotFoundException @JvmOverloads constructor(val entityClass: KClass<*>,
                                                        val parameters: Map<String, Any>,
                                                        override val cause: Throwable? = null
) : RuntimeException("${entityClass.simpleName!!.toLowerCase()} not found. " +
    "Params ${parameters.map { "[${it.key}:${it.value}]"}.reduce { l, r -> "$l $r"} }") {

    @JvmOverloads constructor(entityClass: KClass<*>,
                              id: UUID,
                              cause: Throwable? = null
    ) : this(
        entityClass = entityClass,
        parameters = hashMapOf("id" to id),
        cause = cause
    )

}
