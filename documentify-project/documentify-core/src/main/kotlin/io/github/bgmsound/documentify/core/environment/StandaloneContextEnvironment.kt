package io.github.bgmsound.documentify.core.environment

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.convert.converter.Converter

interface StandaloneContextEnvironment<T : StandaloneContextEnvironment<T>> : DocumentContextEnvironment {

    fun codec(codec: ObjectMapper): T

    fun controller(controller: Any): T

    fun controllers(vararg controllers: Any): T

    fun controllers(controllers: List<Any>): T

    fun controllerAdvice(controllerAdvice: Any): T

    fun controllerAdvices(vararg controllerAdvices: Any): T

    fun controllerAdvices(controllerAdvices: List<Any>): T

    fun converter(converter: Converter<*, *>): T

    fun converters(vararg converters: Converter<*, *>): T

    fun converters(converters: List<Converter<*, *>>): T

}