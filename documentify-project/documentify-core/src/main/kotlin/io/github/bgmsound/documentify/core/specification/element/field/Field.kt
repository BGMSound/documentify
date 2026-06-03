package io.github.bgmsound.documentify.core.specification.element.field

import io.github.bgmsound.documentify.core.specification.DocumentifyDsl
import io.github.bgmsound.documentify.core.specification.FieldSchema
import io.github.bgmsound.documentify.core.specification.element.SpecElement
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes
import java.time.LocalDate
import java.time.LocalDateTime

@DocumentifyDsl
class Field(
    private val descriptor: FieldDescriptor,
    override val key: String,
    private val childFields: MutableList<Field> = mutableListOf()
) : SpecElement(descriptor), FieldSchema {
    val path: String get() = descriptor.path

    fun childFields(): List<Field> = childFields

    infix fun type(type: DocsFieldType): Field {
        check(type == OBJECT || type == ARRAY || childFields.isEmpty()) {
            "Field '$key' has child fields and cannot change to a non-container type"
        }
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

    private fun requireContainer() {
        check(canHaveChild()) { "Field '$key' must be an object or array to have child fields" }
    }

    infix fun with(childFieldsCustomizer: Field.() -> Unit): Field {
        childFieldsCustomizer.invoke(this)
        return this
    }

    fun type(type: DocsFieldType, childFieldsCustomizer: Field.() -> Unit): Field {
        type(type)
        requireContainer()
        return with(childFieldsCustomizer)
    }

    private fun childField(field: Field): Field {
        requireContainer()
        childFields.add(field)
        return field
    }

    override fun field(path: String, description: String, sample: Any): Field {
        return field(path, description, sample) {}
    }

    override fun field(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field {
        val field = newField(sample.javaClass, buildPath(path), description, sample, Requirement.REQUIRED)
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
        return buildList {
            add(descriptor)
            childFields.forEach { addAll(it.build()) }
        }
    }

    fun buildPath(path: String): String {
        var parent = this.path
        parent = if (descriptor.type == ARRAY.type) {
            "$parent[]."
        } else if (parent.isNotEmpty() && parent.isNotBlank()) {
            "$parent."
        } else {
            this.path
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
            when {
                sample is Collection<*> || clazz.isArray -> descriptor.type(ARRAY.type)
                sample is Map<*, *> -> descriptor.type(OBJECT.type)
                sample is String -> descriptor.type(STRING.type)
                sample is Enum<*> -> descriptor.type(STRING.type)
                sample is Boolean -> descriptor.type(BOOLEAN.type)
                sample is Number -> descriptor.type(NUMBER.type)
                sample is LocalDate -> descriptor.type(DATE.type)
                sample is LocalDateTime -> descriptor.type(DATETIME.type)
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

        private val BRACKET_KEY = Regex("""\['([^']*)']$""")

        private fun extractKeyFromPath(path: String): String {
            BRACKET_KEY.find(path)?.let { return it.groupValues[1] }
            return path.substringAfterLast(".").substringBefore("[")
        }
    }
}