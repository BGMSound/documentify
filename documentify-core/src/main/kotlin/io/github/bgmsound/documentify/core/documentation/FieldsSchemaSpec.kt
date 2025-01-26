package io.github.bgmsound.documentify.core.documentation

import io.github.bgmsound.documentify.core.documentation.element.field.Field

interface FieldsSchemaSpec {

    fun field(path: String, description: String): Field

    fun field(description: String): Field {
        return field("", description)
    }

    fun field(path: String, description: String, sample: Any): Field

    fun field(description: String, sample: Any): Field {
        return field("", description, sample)
    }

    fun field(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field

    fun field(description: String, sample: Any, childFields: Field.() -> Unit): Field {
        return field("", description, sample, childFields)
    }

    fun optionalField(path: String, description: String): Field

    fun optionalField(description: String) {
        optionalField("", description)
    }

    fun optionalField(path: String, description: String, sample: Any): Field

    fun optionalField(description: String, sample: Any): Field {
        return optionalField("", description, sample)
    }

    fun optionalField(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field

    fun optionalField(description: String, sample: Any, childFields: Field.() -> Unit): Field {
        return optionalField("", description, sample, childFields)
    }

    fun ignoreField(path: String, description: String): Field

    fun ignoreField(description: String): Field {
        return ignoreField("", description)
    }

    fun ignoreField(path: String, description: String, sample: Any): Field

    fun ignoreField(description: String, sample: Any): Field {
        return ignoreField("", description, sample)
    }

    fun ignoreField(path: String, description: String, sample: Any, childFields: Field.() -> Unit): Field

    fun ignoreField(description: String, sample: Any, childFields: Field.() -> Unit): Field {
        return ignoreField("", description, sample, childFields)
    }

    fun arrayField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun arrayField(description: String, childFields: Field.() -> Unit): Field {
        return arrayField("", description, childFields)
    }

    fun optionalArrayField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun optionalArrayField(description: String, childFields: Field.() -> Unit): Field {
        return optionalArrayField("", description, childFields)
    }

    fun ignoreArrayField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun ignoreArrayField(description: String, childFields: Field.() -> Unit): Field {
        return ignoreArrayField("", description, childFields)
    }

    fun objectField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun objectField(description: String, childFields: Field.() -> Unit): Field {
        return objectField("", description, childFields)
    }

    fun optionalObjectField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun optionalObjectField(description: String, childFields: Field.() -> Unit): Field {
        return optionalObjectField("", description, childFields)
    }

    fun ignoreObjectField(path: String, description: String, childFields: Field.() -> Unit): Field

    fun ignoreObjectField(description: String, childFields: Field.() -> Unit): Field {
        return ignoreObjectField("", description, childFields)
    }

}