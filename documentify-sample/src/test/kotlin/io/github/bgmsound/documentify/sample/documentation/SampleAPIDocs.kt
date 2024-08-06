package io.github.bgmsound.documentify.sample.documentation

import io.github.bgmsound.documentify.core.Documentify
import io.github.bgmsound.documentify.sample.SampleController
import io.restassured.http.Method
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationContextProvider

class SampleAPIDocs : Documentify() {
    private val api = SampleController()

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controllers(api)
        }
    }

    @Test
    fun sampleGetApi() {
        documentation("Sample Get API") {
            information {
                description("this is Sample API description")
                tag("flat")
            }
            requestLine(Method.GET, "/sample/{integerField}") {
                pathVariable("integerField", "integerField", "1")
                queryParameter("stringField", "stringField", "string")
            }
            responseBody {
                field("integerField", "integerField", 1)
                field("stringField", "stringField", "string")
            }
        }
    }

    @Test
    fun samplePostApi() {
        documentation("Sample Post API") {
            information {
                summary("Custom Sample Post API")
                description("this is Sample API description")
                tag("flat")
            }
            requestLine(Method.POST, "/sample")
            requestBody("Sample Post Request") {
                field("integerField", "integerField", 1)
                field("stringField", "stringField", "string")
            }
            responseStatus(HttpStatus.OK)
        }
    }

    @Test
    fun samplePatchApi() {
        documentation("Sample Patch API") {
            information {
                summary("Custom Sample Patch API")
                tag("flat")
            }
            request {
                line(Method.PATCH, "/sample")
                body("Sample Patch Request") {
                    field("integerField", "integerField", 1)
                    field("stringField", "stringField", "string")
                }
            }
            response {
                status(HttpStatus.OK)
                body("Sample Patch Response") {
                    field("integerField", "integerField", 1)
                    field("stringField", "stringField", "string")
                }
            }
        }
    }
}