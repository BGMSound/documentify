package io.github.bgmsound.documentify.core.specification.element

import org.springframework.restdocs.request.ParameterDescriptor

open class Parameter(
    val descriptor: ParameterDescriptor
) : SpecElement(descriptor) {
    override val key: String get() = descriptor.name

    val type: Type = when {
        descriptor.isOptional -> Type.OPTIONAL
        descriptor.isIgnored -> Type.IGNORED
        else -> Type.REQUIRED
    }
}