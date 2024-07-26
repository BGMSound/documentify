package io.github.bgmsound.documentify.core.specification.element

import org.springframework.restdocs.payload.FieldDescriptor

class Field(
    val descriptor: FieldDescriptor
) : SpecElement(descriptor) {

    override val key: String get() = descriptor.path
    val type: Type = when {
        descriptor.isOptional -> Type.OPTIONAL
        descriptor.isIgnored -> Type.IGNORED
        else -> Type.REQUIRED
    }

    fun type(type: DocsFieldType) {
        descriptor.type(type.type)
    }

    enum class Type {
        REQUIRED,
        OPTIONAL,
        IGNORED;
    }
}