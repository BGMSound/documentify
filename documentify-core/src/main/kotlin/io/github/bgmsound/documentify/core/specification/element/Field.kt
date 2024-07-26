package io.github.bgmsound.documentify.core.specification.element

import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.SAMPLE_KEY
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes

class Field(
    private val descriptor: FieldDescriptor
) : SpecElement(descriptor) {
    override val key: String get() = descriptor.path

    val type: Type = when {
        descriptor.isOptional -> Type.OPTIONAL
        descriptor.isIgnored -> Type.IGNORED
        else -> Type.REQUIRED
    }

    infix fun type(type: DocsFieldType) {
        descriptor.type(type.type)
    }

    fun optional() {
        descriptor.optional()
    }

    fun ignored() {
        descriptor.ignored()
    }

    fun build(): List<FieldDescriptor> {
        return listOf(descriptor)
    }

    companion object {
        fun <T> newField(
            clazz: Class<*>,
            path: String,
            description: String,
            sample: T,
            type: Type
        ): Field {
            val descriptor = PayloadDocumentation
                .fieldWithPath(path)
                .description(description)
                .attributes(
                    Attributes.Attribute(SAMPLE_KEY, sample)
                )
            when (type) {
                Type.REQUIRED -> {}
                Type.OPTIONAL -> descriptor.optional()
                Type.IGNORED -> descriptor.ignored()
            }
            return Field(descriptor)
        }
    }
}