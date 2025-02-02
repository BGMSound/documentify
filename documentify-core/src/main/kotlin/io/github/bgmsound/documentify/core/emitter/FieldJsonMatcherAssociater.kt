package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.element.field.Field

object FieldJsonMatcherAssociater {
    fun List<Field>.associatedMatchers(): List<Pair<String, Any>> {
        return filter {
            it.hasSample() || it.canHaveChild() || !it.isIgnored()
        }.flatMap {
            it.associatedMatchers()
        }
    }

    fun Field.associatedMatchers(): List<Pair<String, Any>> {
        if (isIgnored()) {
            return emptyList()
        }
        if (!hasSample() && !canHaveChild()) {
            return emptyList()
        }
        val matchers = mutableListOf<Pair<String, Any>>()
        if (hasSample()) {
            matchers.add("$.${path.replace("[]", "[*]")}" to sample)
        } else {
            if (childFields().isEmpty()) {
                throw IllegalStateException("Field $key must have child fields")
            }
            val childMatchers = childFields().associatedMatchers()
            matchers.addAll(childMatchers)
        }
        return matchers
    }
}