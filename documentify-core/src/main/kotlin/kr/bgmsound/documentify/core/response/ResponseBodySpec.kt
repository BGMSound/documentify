package kr.bgmsound.documentify.core.response

import kr.bgmsound.documentify.core.BodySpec
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Snippet

class ResponseBodySpec : BodySpec() {
    private var _responseFieldsSnippet: ResponseFieldsSnippet? = null

    override fun build(): List<Snippet> {
        if (fields.isEmpty()) {
            return emptyList()
        }
        _responseFieldsSnippet = PayloadDocumentation.responseFields(fields.map { it.descriptor })
        return listOf(_responseFieldsSnippet!!)
    }
}