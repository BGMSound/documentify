package io.github.bgmsound.documentify.mvc.environment

import io.github.bgmsound.documentify.core.specification.schema.document.DocumentSpec
import io.github.bgmsound.documentify.mvc.MvcDocumentContextEnvironment
import io.github.bgmsound.documentify.mvc.emitter.EmitterFactory
import io.github.bgmsound.documentify.mvc.emitter.MvcDocumentEmitter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

class WebApplicationContextEnvironment private constructor(
    private val applicationContext: WebApplicationContext
) : MvcDocumentContextEnvironment {

    companion object {
        fun webApplicationContextEnvironment(context: WebApplicationContext): WebApplicationContextEnvironment {
            return WebApplicationContextEnvironment(context)
        }
    }

    override fun buildEmitter(
        provider: RestDocumentationContextProvider,
        documentSpec: DocumentSpec,
    ): MvcDocumentEmitter {
        val mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(provider))
            .build()

        return EmitterFactory.createMvcEmitter(provider, documentSpec, mockMvc)
    }
}