package io.github.bgmsound.documentify.reactive.emitter

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentResult
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.springframework.test.web.reactive.server.WebTestClient

class ReactiveDocumentResult(
    private val actualResponse: WebTestClient.BodyContentSpec
) : AbstractDocumentResult() {
    override fun expect(jsonPath: String, matcher: Matcher<*>) {
        actualResponse.jsonPath(jsonPath).value(matcher)
    }

    override fun expectValue(jsonPath: String, value: Any) {
        actualResponse.jsonPath(jsonPath).value(Matchers.equalToObject(value))
    }

    companion object {
        fun WebTestClient.BodyContentSpec.validateWith(responseSpec: ResponseSpec) {
            ReactiveDocumentResult(this).validateWith(responseSpec)
        }
    }
}