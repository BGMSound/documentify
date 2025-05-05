package io.github.bgmsound.documentify.mvc.emitter

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentResult
import io.github.bgmsound.documentify.core.emitter.JsonResultMatcher
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import io.github.bgmsound.documentify.mvc.ValidatableMockResponse
import org.hamcrest.Matchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

class MvcDocumentResult(
    private val actualResponse: ValidatableMockResponse
) : AbstractDocumentResult() {
    override fun validateJsonPath(jsonResultMatcher: JsonResultMatcher) {
        val jsonPath = jsonResultMatcher.jsonPath
        val value = jsonResultMatcher.expectedValue

        if (jsonPath.endsWith("[*]")) {
            if (value !is List<*>) {
                throw IllegalArgumentException("sample value type must be List")
            }
            actualResponse.expect(jsonPath(jsonPath.substringBeforeLast("[*]")).value(Matchers.containsInAnyOrder(*value.toTypedArray())))
        } else if (jsonPath.contains("[*]") && !jsonPath.endsWith("[*]")) {
            actualResponse.expect(jsonPath(jsonPath).value(Matchers.hasItem(value)))
        } else {
            actualResponse.expect(jsonPath(jsonPath).value(value))
        }
    }

    companion object {
        fun ValidatableMockResponse.validateWith(responseSpec: ResponseSpec) {
            MvcDocumentResult(this).validateWith(responseSpec)
        }
    }
}