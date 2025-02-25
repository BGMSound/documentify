package io.github.bgmsound.documentify.core.environment

import com.fasterxml.jackson.databind.ObjectMapper

@Suppress("UNCHECKED_CAST")
class StandaloneContextEnvironmentDelegate<T : StandaloneContextEnvironmentSpec<T>> : StandaloneContextEnvironmentSpec<T> {
    lateinit var environmentSpec: T
    val controllers: MutableList<Any> = mutableListOf()
    val controllerAdvices: MutableList<Any> = mutableListOf()
    var objectMapper: ObjectMapper? = null

    override fun objectMapper(objectMapper: ObjectMapper): T {
        this.objectMapper = objectMapper
        return environmentSpec
    }

    override fun controller(controller: Any): T {
        this.controllers.add(controller)
        return environmentSpec
    }

    override fun controllers(vararg controllers: Any): T {
        this.controllers.addAll(controllers)
        return environmentSpec
    }

    override fun controllers(controllers: List<Any>): T {
        this.controllers.addAll(controllers)
        return environmentSpec
    }

    override fun controllerAdvice(controllerAdvice: Any): T {
        this.controllerAdvices.add(controllerAdvice)
        return environmentSpec
    }

    override fun controllerAdvices(vararg controllerAdvices: Any): T {
        this.controllerAdvices.addAll(controllerAdvices)
        return environmentSpec
    }

    override fun controllerAdvices(controllerAdvices: List<Any>): T {
        this.controllerAdvices.addAll(controllerAdvices)
        return environmentSpec
    }
}