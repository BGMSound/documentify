package io.github.bgmsound.documentify.core.documentation.element.parameter

import io.github.bgmsound.documentify.core.documentation.element.SpecElement
import org.springframework.restdocs.request.ParameterDescriptor

open class Parameter(
    private val descriptor: ParameterDescriptor
) : SpecElement(descriptor) {
    override val key: String get() = descriptor.name

    fun build() = descriptor
}