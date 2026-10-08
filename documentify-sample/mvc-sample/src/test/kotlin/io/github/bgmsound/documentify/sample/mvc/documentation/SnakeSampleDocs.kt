package io.github.bgmsound.documentify.sample.mvc.documentation

import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.mvc.Documentify
import io.github.bgmsound.documentify.sample.mvc.controller.SampleController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationContextProvider
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule

class SnakeSampleDocs : Documentify by Documentify.new() {
    private val api = SampleController()
    private val objectMapper = JsonMapper.builder()
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .addModule(KotlinModule.Builder().build())
        .build()

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controller(api)
            codec(objectMapper)
        }
    }

    @Test
    fun samplePostApi() {
        documentation("Snake Sample Post API") {
            information {
                summary("Custom Sample Post API")
                description("this is Sample API description")
                tag("flat")
            }
            requestLine(Method.POST, "/sample")
            requestBody("Sample Post Request") {
                field("integer_field", "integerField", 1)
                field("string_field", "stringField", "string")
            }
            responseStatus(HttpStatus.OK)
            responseBody {
                field("integer_field", "integerField", 1)
                field("string_field", "stringField", "string")
            }
        }
    }
}