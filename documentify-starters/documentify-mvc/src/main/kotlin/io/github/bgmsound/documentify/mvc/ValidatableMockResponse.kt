package io.github.bgmsound.documentify.mvc

import org.springframework.test.web.servlet.ResultMatcher

interface ValidatableMockResponse {

    fun expect(matcher: ResultMatcher): ValidatableMockResponse

}