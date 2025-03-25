package io.github.bgmsound.documentify.mvc.environment

import io.github.bgmsound.documentify.mvc.AbstractMvcDocumentContextEnvironment
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

class WebApplicationContextEnvironment private constructor(
    private val provider: RestDocumentationContextProvider,
    private val applicationContext: WebApplicationContext
) : AbstractMvcDocumentContextEnvironment() {

    companion object {
        fun webApplicationContextEnvironment(
            provider: RestDocumentationContextProvider,
            context: WebApplicationContext
        ): WebApplicationContextEnvironment {
            return WebApplicationContextEnvironment(provider, context)
        }
    }

    override fun buildMockMvc(): MockMvc {
        return MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(provider))
            .build()
    }
}