package kr.bgmsound.documentify.core.request

import kr.bgmsound.documentify.core.BodySpec
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.snippet.Snippet

class RequestBodySpec : BodySpec() {
    private var _fieldsSnippet: RequestFieldsSnippet? = null

    override fun build(): List<Snippet> {
        if (fields.isEmpty()) {
            return emptyList()
        }
        _fieldsSnippet = PayloadDocumentation.requestFields(fields.map { it.descriptor })
        return listOf(_fieldsSnippet!!)
    }
}