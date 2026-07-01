package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.emitter.AbstractDocumentResult
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matcher
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DocumentResultTest {

    private class CapturingDocumentResult : AbstractDocumentResult() {
        val captured = mutableListOf<Pair<String, Matcher<*>>>()
        override fun expect(jsonPath: String, matcher: Matcher<*>) {
            captured += jsonPath to matcher
        }
    }

    private fun matcherFor(spec: ResponseSpec.() -> Unit): Matcher<*> {
        val response = ResponseSpec().apply(spec)
        val result = CapturingDocumentResult()
        result.validateWith(response)
        return result.captured.single().second
    }

    @Test
    fun `numeric matcher treats integer long and big decimal as equal`() {
        val matcher = matcherFor { body { field("amount", "amount", 1L) } }

        assertThat(matcher.matches(1)).isTrue()
        assertThat(matcher.matches(1L)).isTrue()
        assertThat(matcher.matches(BigDecimal.ONE)).isTrue()
        assertThat(matcher.matches(1.0)).isTrue()
        assertThat(matcher.matches(2)).isFalse()
    }

    @Test
    fun `non numeric value uses strict equality`() {
        val matcher = matcherFor { body { field("name", "name", "John") } }

        assertThat(matcher.matches("John")).isTrue()
        assertThat(matcher.matches("Jane")).isFalse()
    }
}
