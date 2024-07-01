package kr.bgmsound.documentify.core.specification.element

import org.springframework.restdocs.payload.FieldDescriptor

class Field(
    val descriptor: FieldDescriptor
) : SpecElement {

    override val key: String get() = descriptor.path
    val type: Type = when {
        descriptor.isOptional -> Type.OPTIONAL
        descriptor.isIgnored -> Type.IGNORED
        else -> Type.REQUIRED
    }
    override val description = descriptor.description as String
    override val sample get() = descriptor.sample()

    fun type(type: DocsFieldType) {
        descriptor.type(type.type)
    }

    enum class Type {
        REQUIRED,
        OPTIONAL,
        IGNORED;
    }
}