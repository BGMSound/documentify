package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.element.field.Field
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.hamcrest.Matcher
import org.hamcrest.Matchers

abstract class AbstractDocumentResult : DocumentResult {
    abstract fun expect(jsonPath: String, matcher: Matcher<*>)

    abstract fun expectValue(jsonPath: String, value: Any)

    override fun validateWith(responseSpec: ResponseSpec) {
        val matchers = aggregateMatchers(responseSpec.fields)
        if (matchers.isEmpty()) {
            return
        }
        matchers.forEach { matcher ->
            val jsonPath = matcher.jsonPath
            val value = matcher.expectedValue
            if (jsonPath.endsWith("[*]")) {
                if (value !is List<*>) {
                    throw IllegalArgumentException("sample value type must be List")
                }
                expect(jsonPath.substringBeforeLast("[*]"), Matchers.containsInAnyOrder(*value.toTypedArray()))
            } else if (jsonPath.contains("[*]") && !jsonPath.endsWith("[*]")) {
                expect(jsonPath, Matchers.hasItem(value))
            } else {
                expectValue(jsonPath, value)
            }
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