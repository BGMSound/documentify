package kr.bgmsound.documentify.core

import org.springframework.restdocs.payload.FieldDescriptor

class Field(
    val descriptor: FieldDescriptor
) {

    val key: String = descriptor.path
    val type: Type = when {
        descriptor.isOptional -> Type.OPTIONAL
        descriptor.isIgnored -> Type.IGNORED
        else -> Type.REQUIRED
    }
    val description: String = descriptor.description as String
    val sample: Any = descriptor.sample()

    fun type(type: DocsFieldType) {
        descriptor.type(type.type)
    }

    enum class Type {
        REQUIRED,
        OPTIONAL,
        IGNORED;
    }
}