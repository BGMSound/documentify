package kr.bgmsound.documentify.core

import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ExtendWith(RestDocumentationExtension::class)
abstract class Documentify {
    protected lateinit var spec: MockMvcRequestSpecification

    @BeforeEach
    fun setUp(
        webApplicationContext: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider
    ) {
        // TODO: 나중에 유연하게 설정할 수 있도록 변경
        val temporal = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
        spec = given().mockMvc(temporal.build())
    }

    fun documentation(
        name: String,
        specCustomizer: DocumentSpec.() -> Unit,
    ) {
        val documentSpec = DocumentSpec(name).also { specCustomizer(it) }
        val emitter = RestDocsEmitter(documentSpec)
        emitter.emit(spec)
    }
}