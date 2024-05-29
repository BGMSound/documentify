package kr.bgmsound.documentify.core

import kr.bgmsound.documentify.core.request.RequestSpec
import kr.bgmsound.documentify.core.response.ResponseSpec
import org.springframework.restdocs.snippet.Snippet

class DocumentSpec(
    val name: String,
) : APISpec {

    private val snippets = mutableListOf<Snippet>()

    val request: RequestSpec = RequestSpec()
    val response: ResponseSpec = ResponseSpec()

    fun request(specCustomizer: RequestSpec.() -> Unit) {
        request.apply(specCustomizer)
    }

    fun response(specCustomizer: ResponseSpec.() -> Unit) {
        response.apply(specCustomizer)
    }

    override fun build(): List<Snippet> {
         val subSnippets =  buildList {
            addAll(request.build())
            addAll(response.build())
        }
        snippets.addAll(subSnippets)
        return snippets
    }
}