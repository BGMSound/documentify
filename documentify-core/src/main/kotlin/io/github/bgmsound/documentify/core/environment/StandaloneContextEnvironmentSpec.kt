package io.github.bgmsound.documentify.core.environment

import com.fasterxml.jackson.databind.ObjectMapper

interface StandaloneContextEnvironmentSpec<T> {

    fun objectMapper(objectMapper: ObjectMapper): T

    fun controller(controller: Any): T

    fun controllers(vararg controllers: Any): T

    fun controllers(controllers: List<Any>): T

    fun controllerAdvice(controllerAdvice: Any): T

    fun controllerAdvices(vararg controllerAdvices: Any): T

    fun controllerAdvices(controllerAdvices: List<Any>): T

}