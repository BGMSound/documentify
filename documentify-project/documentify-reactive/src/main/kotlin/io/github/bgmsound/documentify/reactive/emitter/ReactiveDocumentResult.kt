package io.github.bgmsound.documentify.reactive.emitter

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentResult
import io.github.bgmsound.documentify.core.emitter.JsonResultMatcher
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.hamcrest.Matchers
import org.springframework.test.web.reactive.server.WebTestClient

class ReactiveDocumentResult(
    private val actualResponse: WebTestClient.BodyContentSpec
) : AbstractDocumentResult() {
    override fun validateJsonPath(jsonResultMatcher: JsonResultMatcher) {
        val jsonPath = jsonResultMatcher.jsonPath
        val value = jsonResultMatcher.expectedValue

        if (jsonPath.endsWith("[*]")) {
            if (value !is List<*>) {
                throw IllegalArgumentException("sample value type must be List")
            }
            actualResponse.jsonPath(jsonPath.substringBeforeLast("[*]")).value(Matchers.containsInAnyOrder(*value.toTypedArray()))
        } else if (jsonPath.contains("[*]") && !jsonPath.endsWith("[*]")) {
            actualResponse.jsonPath(jsonPath).value(Matchers.hasItem(value))
        } else {
            actualResponse.jsonPath(jsonPath).value(Matchers.equalToObject(value))
        }
    }

    companion object {
        fun WebTestClient.BodyContentSpec.validateWith(responseSpec: ResponseSpec) {
            ReactiveDocumentResult(this).validateWith(responseSpec)
        }
    }
}