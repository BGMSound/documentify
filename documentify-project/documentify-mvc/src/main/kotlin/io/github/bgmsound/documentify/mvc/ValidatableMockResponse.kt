package io.github.bgmsound.documentify.mvc

import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.ResultHandler
import org.springframework.test.web.servlet.ResultMatcher

interface ValidatableMockResponse {

    fun expect(matcher: ResultMatcher): ValidatableMockResponse

    fun status(status: HttpStatus): ValidatableMockResponse

    fun apply(handler: ResultHandler, vararg additionalHandlers: ResultHandler): ValidatableMockResponse

    fun assertThat(matcher: ResultMatcher): ValidatableMockResponse

}