package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.element.field.Field
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec

abstract class AbstractDocumentResult : DocumentResult {
    abstract fun validateJsonPath(jsonResultMatcher: JsonResultMatcher)

    override fun validateWith(responseSpec: ResponseSpec) {
        val matchers = aggregateMatchers(responseSpec.fields)
        if (matchers.isEmpty()) {
            return
        }
        matchers.forEach { matcher ->
            validateJsonPath(matcher)
        }
    }

    private fun aggregateMatchers(fields: List<Field>): List<JsonResultMatcher> {
        return fields.filter {
            it.hasSample() || it.canHaveChild() || !it.isIgnored()
        }.flatMap {
            it.aggregateMatchers()
        }
    }

    private fun Field.aggregateMatchers(): List<JsonResultMatcher> {
        if (isIgnored()) {
            return emptyList()
        }
        if (!hasSample() && !canHaveChild()) {
            return emptyList()
        }
        val matchers = mutableListOf<JsonResultMatcher>()
        if (hasSample()) {
            val jsonPath = StringBuilder("$.${path}").apply {
                if (isArray()) {
                    append("[*]")
                }
            }.toString().replace("[]", "[*]")
            matchers.add(JsonResultMatcher.of(jsonPath, sample))
        } else if (childFields().isNotEmpty())  {
            val childMatchers = aggregateMatchers(childFields())
            matchers.addAll(childMatchers)
        }
        return matchers
    }
}