package io.github.bgmsound.documentify.core.specification.element

import org.springframework.restdocs.request.ParameterDescriptor

open class Parameter(
    private val descriptor: ParameterDescriptor
) : SpecElement(descriptor) {
    override val key: String get() = descriptor.name

    fun build() = descriptor
}