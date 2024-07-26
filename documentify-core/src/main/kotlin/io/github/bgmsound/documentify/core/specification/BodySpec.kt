package io.github.bgmsound.documentify.core.specification

import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.SAMPLE_KEY
import io.github.bgmsound.documentify.core.specification.element.Field
import io.github.bgmsound.documentify.core.specification.element.Field.Type
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes

abstract class BodySpec(
    protected val fields: MutableList<Field> = mutableListOf()
) : APISpec {
    private var schemaName: String? = null

    fun schema(name: String) {
        schemaName = name
    }

    fun schema(): String? = schemaName

    inline fun <reified T> field(path: String, description: String, sample: T): Field {
        return putField(T::class.java, path, description, sample, Type.REQUIRED)
    }

    inline fun <reified T> optionalField(path: String, description: String, sample: T): Field {
        return putField(T::class.java, path, description, sample, Type.OPTIONAL)
    }

    inline fun <reified T> ignoreField(path: String, description: String, sample: T): Field {
        return putField(T::class.java, path, description, sample, Type.IGNORED)
    }

    fun fields(): List<Field> = fields

    fun <T> putField(
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
        val field = Field(descriptor)
        putField(field)
        return field
    }

    fun putField(field: Field) {
        fields.add(field)
    }
}
