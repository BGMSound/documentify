package io.github.bgmsound.documentify.sample.documentation

import io.github.bgmsound.documentify.core.Documentify
import io.github.bgmsound.documentify.core.documentation.specification.Method
import io.github.bgmsound.documentify.sample.ErrorController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider

class ErrorDocs : Documentify() {
    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controller(ErrorController())
        }
    }


    @Test
    fun sampleGetApi() {
        documentation("Error Sample Get API") {
            information {
                description("this is Error Sample API description")
                tag("error")
            }
            requestLine(Method.GET, "/error") {
                queryParameter("error", "boolean", "false")
            }
            responseBody {
                field("integerField", "this is error code", 1)
                field("stringField", "this is error message", "error message")
            }
        }
    }

    @Test
    fun errorSampleGetApi() {
        documentation("Error Sample Get API When Error Occurred") {
            requestLine(Method.GET, "/error") {
                queryParameter("error", "boolean", "true")
            }
            responseLine(400)
            responseBody {
                field("code", "this is error code", 1)
                field("message", "this is error message", "error message")
            }
        }
    }
}