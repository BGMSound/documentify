package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.springframework.test.web.reactive.server.WebTestClient

class WebTestClientDocumentResult(
    private val actualResponse: WebTestClient.BodyContentSpec
) : AbstractDocumentResult() {
    @Suppress("UNCHECKED_CAST")
    override fun expect(jsonPath: String, matcher: Matcher<*>) {
        actualResponse.jsonPath(jsonPath).value<Any> { actual: Any? ->
            MatcherAssert.assertThat("JSON path \"$jsonPath\"", actual, matcher as Matcher<Any?>)
        }
    }

    companion object {
        fun WebTestClient.BodyContentSpec.validateWith(responseSpec: ResponseSpec) {
            WebTestClientDocumentResult(this).validateWith(responseSpec)
        }
    }
}