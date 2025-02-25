package io.github.bgmsound.documentify.sample.reactive.documentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.reactive.Documentify
import io.github.bgmsound.documentify.sample.reactive.SampleController
import kotlinx.coroutines.test.runTest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationContextProvider

class SnakeSampleDocs : Documentify() {
    private val api = SampleController()
    private val objectMapper = ObjectMapper().apply {
        propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
        registerModules(KotlinModule.Builder().build())
    }

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controller(api)
            objectMapper(objectMapper)
        }
    }

    @Test
    fun samplePostApi() = runTest {
        documentation("Snake Sample Post API") {
            information {
                summary("Snake Sample Post API")
                description("this is Sample API description")
                tag("flat")
            }
            requestLine(Method.POST, "/sample")
            requestBody {
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