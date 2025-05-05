package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.element.SpecElement
import io.github.bgmsound.documentify.core.specification.element.field.Field

object DefaultDocumentSpecSampleAggregator : DocumentSpecSampleAggregator {
    @Suppress("UNCHECKED_CAST")
    override fun <T : SpecElement> aggregate(specElements: List<T>): Map<String, Any> {
        if (specElements.isEmpty()) {
            return emptyMap()
        }
        if (!specElements.isField()) {
            return specElements.associate {
                it.key to it.sample
            }
        }
        val fieldElements = specElements as List<Field>
        return fieldElements.filter {
            it.hasSample() || it.canHaveChild() || !it.isIgnored()
        }.associate {
            it.aggregateSample()
        }
    }

    private fun Field.aggregateSample(): Pair<String, Any> {
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
            val sample = childFields().associate { it.aggregateSample() }
            if (isArray()) {
                listOf(sample)
            } else {
                sample
            }
        }
    }

    private fun List<SpecElement>.isField(): Boolean {
        return all {
            it is Field
        }
    }
}