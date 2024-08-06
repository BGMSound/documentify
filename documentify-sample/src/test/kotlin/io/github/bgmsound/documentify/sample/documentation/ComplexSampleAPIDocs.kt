package io.github.bgmsound.documentify.sample.documentation

import io.github.bgmsound.documentify.core.Documentify
import io.github.bgmsound.documentify.sample.ComplexSampleController
import io.restassured.http.Method
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider

class ComplexSampleAPIDocs : Documentify() {
    private val api = ComplexSampleController()

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controllers(api)
        }
    }

    @Test
    fun complexSampleGetApi() {
        documentation("Complex Sample Get API") {
            information {
                description("this is Complex Sample API description")
                tag("complex")
            }
            requestLine(Method.GET, "/complex-sample")
            responseBody {
                objectField("nestedField", "this is nested field") {
                    objectField("nestedField", "this is nested field") {
                        field("integerField", "this is nested field integerField", 1)
                        field("stringField", "this is nested field stringField", "nested sample")
                    }
                    arrayField("listedField", "this is listed field") {
                        field("integerField", "this is listed field integerField", 1)
                        field("stringField", "this is listed field stringField", "nested sample 2")
                    }
                }
            }
        }
    }
}