package io.github.bgmsound.documentify.core.environment

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.convert.converter.Converter

@Suppress("UNCHECKED_CAST")
abstract class AbstractStandaloneContextEnvironment<T : StandaloneContextEnvironment<T>> : StandaloneContextEnvironment<T>, AbstractDocumentContextEnvironment() {
    protected val controllers: MutableList<Any> = mutableListOf()
    protected val controllerAdvices: MutableList<Any> = mutableListOf()
    protected val converters: MutableList<Converter<*, *>> = mutableListOf()
    protected var codec: ObjectMapper? = null

    override fun codec(codec: ObjectMapper): T {
        this.codec = codec
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

    override fun converter(converter: Converter<*, *>): T {
        this.converters.add(converter)
        return this as T
    }

    override fun converters(vararg converters: Converter<*, *>): T {
        this.converters.addAll(converters)
        return this as T
    }

    override fun converters(converters: List<Converter<*, *>>): T {
        this.converters.addAll(converters)
        return this as T
    }
}