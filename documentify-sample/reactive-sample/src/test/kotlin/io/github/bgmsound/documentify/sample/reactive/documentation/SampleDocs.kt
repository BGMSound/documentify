package io.github.bgmsound.documentify.sample.reactive.documentation


import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.reactive.Documentify
import io.github.bgmsound.documentify.sample.reactive.SampleController
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationContextProvider

class SampleDocs : Documentify() {
    private val api = SampleController()

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controller(api)
        }
    }

    @Test
    fun sampleGetApi() = runTest {
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
            alternativeResponse {
                status(400)
                body {
                    field("integerField", "this is error code", 1)
                    field("stringField", "this is error message", "error message")
                }
            }
        }
    }

    @Test
    fun samplePostApi() = runTest {
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
            responseBody {
                field("integerField", "integerField", 1)
                field("stringField", "stringField", "string")
            }
        }
    }

    @Test
    fun samplePatchApi() = runTest {
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

    @Test
    fun sampleDeleteApi() = runTest {
        documentation("Sample Delete API") {
            information {
                tag("flat")
            }
            requestLine(Method.DELETE, "/sample/{id}") {
                pathVariable("id", "id", "id")
            }
        }
    }
}