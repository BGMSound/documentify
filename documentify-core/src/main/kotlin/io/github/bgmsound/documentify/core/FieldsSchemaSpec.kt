package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.specification.element.Field

interface FieldsSchemaSpec {

    fun field(path: String, description: String): Field

    fun field(path: String, description: String, sample: Any): Field

    fun field(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field

    fun optionalField(path: String, description: String): Field

    fun optionalField(path: String, description: String, sample: Any): Field

    fun optionalField(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field

    fun ignoreField(path: String, description: String): Field

    fun ignoreField(path: String, description: String, sample: Any): Field

    fun ignoreField(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field

    fun arrayField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun optionalArrayField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun ignoreArrayField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun objectField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun optionalObjectField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun ignoreObjectField(path: String, description: String, childFields: Field.() -> Unit): Field
}