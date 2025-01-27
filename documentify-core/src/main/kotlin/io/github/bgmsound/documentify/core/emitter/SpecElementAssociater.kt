package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.element.SpecElement
import io.github.bgmsound.documentify.core.specification.element.field.Field

object SpecElementAssociater {
    fun List<Field>.associatedFieldSample(): Map<String, Any> {
        return filter {
            it.hasSample() || it.canHaveChild() || !it.isIgnored()
        }.associate {
            it.associatedSample()
        }
    }

    fun Field.associatedSample(): Pair<String, Any> {
        if (isIgnored()) {
            throw IllegalStateException("can't associate ignored field $key")
        }
        if (!hasSample() && !canHaveChild()) {
            throw IllegalStateException("Field $key must have sample or child fields")
        }
        return key to if (hasSample()) {
            sample
        } else {
            if (childFields().isEmpty()) {
                throw IllegalStateException("Field $key must have child fields")
            }
            val sample = childFields().associate { it.associatedSample() }
            if (isArray()) {
                listOf(sample)
            } else {
                sample
            }
        }
    }

    fun List<SpecElement>.associatedSample(): Map<String, Any> {
        return associate {
            it.key to it.sample
        }
    }
}