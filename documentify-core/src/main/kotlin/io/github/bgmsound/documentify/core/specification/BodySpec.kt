package io.github.bgmsound.documentify.core.specification

import io.github.bgmsound.documentify.core.specification.element.Field
import io.github.bgmsound.documentify.core.specification.element.SpecElement.Type

abstract class BodySpec(
    protected val fields: MutableList<Field> = mutableListOf(),
) : APISpec {
    private var schemaName: String? = null

    fun schema(name: String) {
        schemaName = name
    }

    fun schema(): String? = schemaName

    inline fun <reified T> field(path: String, description: String, sample: T): Field {
        val field = Field.newField(T::class.java, path, description, sample, Type.REQUIRED)
        return putField(field)
    }

    inline fun <reified T> optionalField(path: String, description: String, sample: T): Field {
        val field = Field.newField(T::class.java, path, description, sample, Type.OPTIONAL)
        return putField(field)
    }

    inline fun <reified T> ignoreField(path: String, description: String, sample: T): Field {
        val field = Field.newField(T::class.java, path, description, sample, Type.IGNORED)
        return putField(field)
    }

    fun fields(): List<Field> = fields

    fun putField(field: Field): Field {
        fields.add(field)
        return field
    }
}
