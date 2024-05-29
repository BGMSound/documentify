package kr.bgmsound.documentify.core

import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension::class)
abstract class Documentify {
    @LocalServerPort
    protected var port: Int = -1
    private lateinit var spec: RequestSpecification

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        this.spec = RequestSpecBuilder()
            .setPort(port)
            .addFilter(documentationConfiguration(restDocumentation))
            .build()
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