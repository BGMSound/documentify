package io.github.bgmsound.documentify.core.specification

import io.github.bgmsound.documentify.core.FieldsSchemaSpec
import io.github.bgmsound.documentify.core.specification.element.ARRAY
import io.github.bgmsound.documentify.core.specification.element.Field
import io.github.bgmsound.documentify.core.specification.element.OBJECT
import io.github.bgmsound.documentify.core.specification.element.SpecElement.Requirement

abstract class BodySpec(
    protected val fields: MutableList<Field> = mutableListOf()
) : APISpec, FieldsSchemaSpec {
    private var schemaName: String? = null

    fun schema(name: String) {
        schemaName = name
    }

    fun schema(): String? = schemaName

    override fun field(path: String, description: String, sample: Any): Field {
        return field(path, description, sample) {}
    }

    override fun field(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field {
        val field = Field.newField(sample.javaClass, path, description, sample, Requirement.REQUIRED)
        return putField(field).with(childFields)
    }

    override fun field(path: String, description: String): Field {
        val field = Field.newField(path, description, Requirement.REQUIRED)
        return putField(field)
    }

    override fun optionalField(path: String, description: String, sample: Any): Field {
        return optionalField(path, description, sample) {}
    }

    override fun optionalField(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field {
        val field = Field.newField(sample.javaClass, path, description, sample, Requirement.OPTIONAL)
        return putField(field).with(childFields)
    }

    override fun optionalField(path: String, description: String): Field {
        val field = Field.newField(path, description, Requirement.OPTIONAL)
        return putField(field)
    }

    override fun ignoreField(path: String, description: String, sample: Any): Field {
        return ignoreField(path, description, sample) {}
    }

    override fun ignoreField(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field {
        val field = Field.newField(sample.javaClass, path, description, sample, Requirement.IGNORED)
        return putField(field).with(childFields)
    }

    override fun ignoreField(path: String, description: String): Field {
        val field = Field.newField(path, description, Requirement.IGNORED)
        return putField(field)
    }

    override fun arrayField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = Field.newField(path, description, Requirement.REQUIRED).type(ARRAY)
        return putField(field).with(childFields)
    }

    override fun optionalArrayField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = Field.newField(path, description, Requirement.OPTIONAL).type(ARRAY)
        return putField(field).with(childFields)
    }

    override fun ignoreArrayField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = Field.newField(path, description, Requirement.IGNORED).type(ARRAY)
        return putField(field).with(childFields)
    }

    override fun objectField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = Field.newField(path, description, Requirement.REQUIRED).type(OBJECT)
        return putField(field).with(childFields)
    }

    override fun optionalObjectField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = Field.newField(path, description, Requirement.OPTIONAL).type(OBJECT)
        return putField(field).with(childFields)
    }

    override fun ignoreObjectField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = Field.newField(path, description, Requirement.IGNORED).type(OBJECT)
        return putField(field).with(childFields)
    }

    fun fields(): List<Field> = fields

    private fun putField(field: Field): Field {
        fields.add(field)
        return field
    }
}
