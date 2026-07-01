package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.hamcrest.Matcher
import org.springframework.test.web.reactive.server.WebTestClient

class WebTestClientDocumentResult(
    private val actualResponse: WebTestClient.BodyContentSpec
) : AbstractDocumentResult() {
    override fun expect(jsonPath: String, matcher: Matcher<*>) {
        actualResponse.jsonPath(jsonPath).value(matcher)
    }

    companion object {
        fun WebTestClient.BodyContentSpec.validateWith(responseSpec: ResponseSpec) {
            WebTestClientDocumentResult(this).validateWith(responseSpec)
        }
    }
}