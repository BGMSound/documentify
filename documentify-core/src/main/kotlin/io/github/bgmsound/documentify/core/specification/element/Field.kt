package io.github.bgmsound.documentify.core.specification.element

import io.github.bgmsound.documentify.core.FieldsSchemaSpec
import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.SAMPLE_KEY
import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.isPrimitiveOrWrapper
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes

class Field(
    private val descriptor: FieldDescriptor,
    override val key: String,
    private val childFields: MutableList<Field> = mutableListOf()
) : SpecElement(descriptor), FieldsSchemaSpec {
    val path: String get() = descriptor.path

    fun childFields(): List<Field> = childFields

    infix fun type(type: DocsFieldType): Field {
        descriptor.type(type.type)
        return this
    }

    fun optional() {
        descriptor.optional()
    }

    fun ignored() {
        descriptor.ignored()
    }

    fun isOptional(): Boolean {
        return descriptor.isOptional
    }

    fun isIgnored(): Boolean {
        return descriptor.isIgnored
    }

    fun canHaveChild(): Boolean {
        return descriptor.type == OBJECT.type || descriptor.type == ARRAY.type
    }

    fun isObject(): Boolean {
        return descriptor.type == OBJECT.type
    }

    fun isArray(): Boolean {
        return descriptor.type == ARRAY.type
    }

    fun with(childFieldsCustomizer: Field.() -> Unit): Field {
        childFieldsCustomizer.invoke(this)
        return this
    }

    fun type(type: DocsFieldType, childFieldsCustomizer: Field.() -> Unit): Field {
        type(type)
        if (!canHaveChild()) {
            throw IllegalArgumentException("Field $key can't have child fields")
        }
        return with(childFieldsCustomizer)
    }

    fun childField(field: Field): Field {
        if (!canHaveChild()) {
            throw IllegalArgumentException("Field $key can't have child fields")
        }
        childFields.add(field)
        return field
    }

    override fun field(path: String, description: String, sample: Any): Field {
        return field(path, description, sample) {}
    }

    override fun field(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field {
        val field = newField(path.javaClass, buildPath(path), description, sample, Requirement.REQUIRED)
        return childField(field).with(childFields)
    }

    override fun field(path: String, description: String): Field {
        val field = newField(buildPath(path), description, Requirement.REQUIRED)
        return childField(field)
    }

    override fun optionalField(path: String, description: String, sample: Any): Field {
        return optionalField(path, description, sample) {}
    }

    override fun optionalField(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field {
        val field = newField(sample.javaClass, buildPath(path), description, sample, Requirement.OPTIONAL)
        return childField(field).with(childFields)
    }

    override fun optionalField(path: String, description: String): Field {
        val field = newField(buildPath(path), description, Requirement.OPTIONAL)
        return childField(field)
    }

    override fun ignoreField(path: String, description: String, sample: Any): Field {
        return ignoreField(path, description, sample) {}
    }

    override fun ignoreField(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field {
        val field = newField(sample.javaClass, buildPath(path), description, sample, Requirement.IGNORED)
        return childField(field).with(childFields)
    }

    override fun ignoreField(path: String, description: String): Field {
        val field = newField(buildPath(path), description, Requirement.IGNORED)
        return childField(field)
    }

    override fun arrayField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = newField(buildPath(path), description, Requirement.REQUIRED).type(ARRAY)
        return childField(field).with(childFields)
    }

    override fun optionalArrayField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = newField(buildPath(path), description, Requirement.OPTIONAL).type(ARRAY)
        return childField(field).with(childFields)
    }

    override fun ignoreArrayField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = newField(buildPath(path), description, Requirement.IGNORED).type(ARRAY)
        return childField(field).with(childFields)
    }

    override fun objectField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = newField(buildPath(path), description, Requirement.REQUIRED).type(OBJECT)
        return childField(field).with(childFields)
    }

    override fun optionalObjectField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = newField(buildPath(path), description, Requirement.OPTIONAL).type(OBJECT)
        return childField(field).with(childFields)
    }

    override fun ignoreObjectField(path: String, description: String, childFields: Field.() -> Unit): Field {
        val field = newField(buildPath(path), description, Requirement.IGNORED).type(OBJECT)
        return childField(field).with(childFields)
    }

    fun build(): List<FieldDescriptor> {
        if (!canHaveChild() && childFields.isNotEmpty()) {
            throw IllegalStateException("Field $key can't have child fields")
        }
        return buildList {
            add(descriptor)
            childFields.forEach { addAll(it.build()) }
        }
    }

    fun buildPath(path: String): String {
        var parent = this.path
        parent = if (descriptor.type == ARRAY.type) {
            "$parent[]."
        } else {
            "$parent."
        }
        return "$parent$path"
    }

    companion object {
        fun newField(
            clazz: Class<*>,
            path: String,
            description: String,
            sample: Any,
            requirement: Requirement,
        ): Field {
            val descriptor = PayloadDocumentation
                .fieldWithPath(path)
                .description(description)
                .attributes(
                    Attributes.Attribute(SAMPLE_KEY, sample)
                )
            when (requirement) {
                Requirement.REQUIRED -> {}
                Requirement.OPTIONAL -> descriptor.optional()
                Requirement.IGNORED -> descriptor.ignored()
            }
            if (sample is Collection<*> || clazz.isArray) {
                descriptor.type(ARRAY.type)
            } else if (sample is Map<*, *>) {
                descriptor.type(OBJECT.type)
            } else if (sample is String) {
                descriptor.type(STRING.type)
            }
            return Field(descriptor, extractKeyFromPath(path))
        }

        fun newField(path: String, description: String, requirement: Requirement): Field {
            val descriptor = PayloadDocumentation
                .fieldWithPath(path)
                .description(description)
            when (requirement) {
                Requirement.REQUIRED -> {}
                Requirement.OPTIONAL -> descriptor.optional()
                Requirement.IGNORED -> descriptor.ignored()
            }
            return Field(descriptor, extractKeyFromPath(path))
        }

        private fun extractKeyFromPath(path: String): String {
            val lastKey = path.substringAfterLast(".")
            val parentKey = path.substringBeforeLast(".")
            return if (lastKey.contains("'")) {
                val parentKeyLast = parentKey.substringAfterLast(".")
                "$parentKeyLast.$lastKey"
            } else {
                lastKey
            }
        }
    }
}