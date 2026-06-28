package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.element.field.Field
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import java.math.BigDecimal

abstract class AbstractDocumentResult : DocumentResult {
    abstract fun expect(jsonPath: String, matcher: Matcher<*>)

    override fun validateWith(responseSpec: ResponseSpec) {
        val matchers = aggregateMatchers(responseSpec.fields)
        if (matchers.isEmpty()) {
            return
        }
        matchers.forEach { matcher ->
            val jsonPath = matcher.jsonPath
            val value = matcher.expectedValue
            when {
                jsonPath.endsWith("[*]") -> {
                    if (value !is List<*>) {
                        throw IllegalArgumentException("sample value type must be List")
                    }
                    expect(
                        jsonPath.substringBeforeLast("[*]"),
                        Matchers.containsInAnyOrder(value.map { equalityMatcher(it) })
                    )
                }
                jsonPath.contains("[*]") -> {
                    expect(jsonPath, Matchers.hasItem(equalityMatcher(value)))
                }
                else -> {
                    expect(jsonPath, equalityMatcher(value))
                }
            }
        }
    }

    private fun equalityMatcher(expected: Any?): Matcher<Any?> {
        if (expected is Number) {
            val expectedDecimal = BigDecimal(expected.toString())
            return object : BaseMatcher<Any?>() {
                override fun matches(actual: Any?): Boolean {
                    val number = actual as? Number ?: return false
                    return runCatching {
                        BigDecimal(number.toString()).compareTo(expectedDecimal) == 0
                    }.getOrDefault(false)
                }

                override fun describeTo(description: Description) {
                    description.appendText("a number numerically equal to ").appendValue(expected)
                }
            }
        }
        return object : BaseMatcher<Any?>() {
            override fun matches(actual: Any?): Boolean = actual == expected

            override fun describeTo(description: Description) {
                description.appendValue(expected)
            }
        }
    }

    private fun aggregateMatchers(fields: List<Field>): List<ExpectedJsonValue> {
        return fields.filter {
            it.hasSample() || it.canHaveChild() || !it.isIgnored()
        }.flatMap {
            it.aggregateMatchers()
        }
    }

    private fun Field.aggregateMatchers(): List<ExpectedJsonValue> {
        if (isIgnored()) {
            return emptyList()
        }
        if (!hasSample() && !canHaveChild()) {
            return emptyList()
        }
        val matchers = mutableListOf<ExpectedJsonValue>()
        if (hasSample()) {
            val jsonPath = StringBuilder("$.${path}").apply {
                if (isArray()) {
                    append("[*]")
                }
            }.toString().replace("[]", "[*]")
            matchers.add(ExpectedJsonValue.of(jsonPath, sample))
        } else if (childFields().isNotEmpty())  {
            val childMatchers = aggregateMatchers(childFields())
            matchers.addAll(childMatchers)
        }
        return matchers
    }
}