package io.github.bgmsound.documentify.mvc.emitter

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentResult
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import io.github.bgmsound.documentify.mvc.ValidatableMockResponse
import org.hamcrest.Matcher
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

class MvcDocumentResult(
    private val actualResponse: ValidatableMockResponse
) : AbstractDocumentResult() {
    override fun expect(jsonPath: String, matcher: Matcher<*>) {
        actualResponse.expect(jsonPath(jsonPath).value(matcher))
    }

    override fun expectValue(jsonPath: String, value: Any) {
        actualResponse.expect(jsonPath(jsonPath).value(value))
    }

    companion object {
        fun ValidatableMockResponse.validateWith(responseSpec: ResponseSpec) {
            MvcDocumentResult(this).validateWith(responseSpec)
        }
    }
}