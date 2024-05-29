package kr.bgmsound.documentify.core

import kr.bgmsound.documentify.core.request.RequestSpec
import kr.bgmsound.documentify.core.response.ResponseSpec
import org.springframework.restdocs.snippet.Snippet

class DocumentSpec(
    val name: String,
) : Spec {

    private val snippets = mutableListOf<Snippet>()

    private val requestSpec: RequestSpec = RequestSpec()
    private val responseSpec: ResponseSpec = ResponseSpec()

    val url get() = requestSpec.url
    val method get() = requestSpec.method
    val statusCode get() = responseSpec.statusCode

    fun request(specCustomizer: RequestSpec.() -> Unit) {
        requestSpec.apply(specCustomizer)
    }

    fun response(specCustomizer: ResponseSpec.() -> Unit) {
        responseSpec.apply(specCustomizer)
    }

    override fun build(): List<Snippet> {
         val subSnippets =  buildList {
            addAll(requestSpec.build())
            addAll(responseSpec.build())
        }
        snippets.addAll(subSnippets)
        return snippets
    }
}