package io.github.bgmsound.documentify.core.emitter

import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseSpec
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.snippet.Snippet

abstract class AbstractDocumentEmitter(
    protected val provider: RestDocumentationContextProvider,
    protected val documentSpec: DocumentSpec,
    protected val sampleAggregator: DocumentSpecSampleAggregator = DefaultDocumentSpecSampleAggregator
) : DocumentEmitter {
    protected fun ResponseSpec.buildResource(index: Int): Snippet {
        val resourceBuilder = ResourceSnippetParameters.builder()
        if (documentSpec.tags.isNotEmpty()) {
            resourceBuilder.tags(*documentSpec.tags.toTypedArray())
        }
        if (this.fields.isNotEmpty()) {
            if (this.schema == null) {
                resourceBuilder.responseSchema(Schema.schema("${documentSpec.name}-Response-case${index + 1}"))
            } else {
                resourceBuilder.responseSchema(Schema.schema(this.schema!!))
            }
        }
        return ResourceDocumentation.resource(resourceBuilder.build())
    }
}
