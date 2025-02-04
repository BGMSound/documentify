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
            val jsonPath = StringBuilder("$.${path}").apply {
                if (isArray()) {
                    append("[*]")
                }
            }.toString().replace("[]", "[*]")
            matchers.add(jsonPath to sample)
        } else if (childFields().isNotEmpty())  {
            val childMatchers = childFields().associatedMatchers()
            matchers.addAll(childMatchers)
        }
        return matchers
    }
}