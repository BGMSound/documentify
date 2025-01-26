package io.github.bgmsound.documentify.core.documentation.specification.response

import io.github.bgmsound.documentify.core.documentation.DocumentableSpec
import org.springframework.http.HttpStatus
import org.springframework.restdocs.snippet.Snippet

class ResponseLineSpec(
    private var statusCode: Int
) : DocumentableSpec {
    constructor(statusCode: HttpStatus) : this(statusCode.value())

    fun statusCode(): Int = statusCode

    fun statusCode(statusCode: HttpStatus) = run { this.statusCode = statusCode.value() }

    fun statusCode(statusCode: Int) = run { this.statusCode = statusCode }

    override fun build(): List<Snippet> {
        return emptyList()
    }
}