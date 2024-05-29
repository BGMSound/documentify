package kr.bgmsound.documentify.core.response

import kr.bgmsound.documentify.core.Spec
import org.springframework.http.HttpStatus
import org.springframework.restdocs.snippet.Snippet

class ResponseLineSpec(
    private var statusCode: Int
) : Spec {
    constructor(statusCode: HttpStatus) : this(statusCode.value())

    fun statusCode(): Int = statusCode

    fun statusCode(statusCode: HttpStatus) = run { this.statusCode = statusCode.value() }

    fun statusCode(statusCode: Int) = run { this.statusCode = statusCode }

    override fun build(): List<Snippet> {
        return emptyList()
    }
}