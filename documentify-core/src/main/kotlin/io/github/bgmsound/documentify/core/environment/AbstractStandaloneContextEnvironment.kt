package io.github.bgmsound.documentify.core.environment

@Suppress("UNCHECKED_CAST")
abstract class AbstractStandaloneContextEnvironment<T : AbstractStandaloneContextEnvironment<T>> : DocumentContextEnvironment {
    protected val controllers: MutableList<Any> = mutableListOf()
    protected val controllerAdvices: MutableList<Any> = mutableListOf()

    fun controller(controller: Any): T {
        this.controllers.add(controller)
        return this as T
    }

    fun controllers(vararg controllers: Any): T {
        this.controllers.addAll(controllers)
        return this as T
    }

    fun controllers(controllers: List<Any>): T {
        this.controllers.addAll(controllers)
        return this as T
    }

    fun controllerAdvice(controllerAdvice: Any): T {
        this.controllerAdvices.add(controllerAdvice)
        return this as T
    }

    fun controllerAdvices(vararg controllerAdvices: Any): T {
        this.controllerAdvices.addAll(controllerAdvices)
        return this as T
    }

    fun controllerAdvices(controllerAdvices: List<Any>): T {
        this.controllerAdvices.addAll(controllerAdvices)
        return this as T
    }
}