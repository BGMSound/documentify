package io.github.bgmsound.documentify.mvc

import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.ResultHandler
import org.springframework.test.web.servlet.ResultMatcher

class ValidatableMockMvcResponseAdapter private constructor(
    private val restAssuredMockResponse: ValidatableMockMvcResponse
) : ValidatableMockResponse {

    override fun expect(matcher: ResultMatcher): ValidatableMockResponse {
        restAssuredMockResponse.expect(matcher)
        return this
    }

    fun status(status: HttpStatus): ValidatableMockResponse {
        restAssuredMockResponse.statusCode(status.value())
        return this
    }

    fun apply(handler: ResultHandler, vararg additionalHandlers: ResultHandler): ValidatableMockResponse {
        restAssuredMockResponse.apply(handler, *additionalHandlers)
        return this
    }

    fun assertThat(matcher: ResultMatcher): ValidatableMockResponse {
        restAssuredMockResponse.assertThat(matcher)
        return this
    }

    companion object {
        fun of(restAssuredMockResponse: ValidatableMockMvcResponse): ValidatableMockResponse {
            return ValidatableMockMvcResponseAdapter(restAssuredMockResponse)
        }
    }
}