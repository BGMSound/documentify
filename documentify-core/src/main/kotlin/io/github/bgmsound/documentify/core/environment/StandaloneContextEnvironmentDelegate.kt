package io.github.bgmsound.documentify.core.environment

import com.fasterxml.jackson.databind.ObjectMapper

@Suppress("UNCHECKED_CAST")
class StandaloneContextEnvironmentDelegate<T : StandaloneContextEnvironmentSpec<T>> : StandaloneContextEnvironmentSpec<T> {
    val controllers: MutableList<Any> = mutableListOf()
    val controllerAdvices: MutableList<Any> = mutableListOf()
    var objectMapper: ObjectMapper? = null

    override fun objectMapper(objectMapper: ObjectMapper): T {
        this.objectMapper = objectMapper
        return this as T
    }

    override fun controller(controller: Any): T {
        this.controllers.add(controller)
        return this as T
    }

    override fun controllers(vararg controllers: Any): T {
        this.controllers.addAll(controllers)
        return this as T
    }

    override fun controllers(controllers: List<Any>): T {
        this.controllers.addAll(controllers)
        return this as T
    }

    override fun controllerAdvice(controllerAdvice: Any): T {
        this.controllerAdvices.add(controllerAdvice)
        return this as T
    }

    override fun controllerAdvices(vararg controllerAdvices: Any): T {
        this.controllerAdvices.addAll(controllerAdvices)
        return this as T
    }

    override fun controllerAdvices(controllerAdvices: List<Any>): T {
        this.controllerAdvices.addAll(controllerAdvices)
        return this as T
    }
}