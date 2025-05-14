package io.github.bgmsound.documentify.sample.mvc.documentation

import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.mvc.Documentify
import io.github.bgmsound.documentify.sample.mvc.controller.ErrorController
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
                queryParameter("throw", "boolean", "false")
            }
            responseBody {
                field("integerField", "this is error code", 200)
                field("stringField", "this is error message", "ok")
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
}