package io.github.bgmsound.documentify.sample.mvc.documentation

import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.mvc.Documentify
import io.github.bgmsound.documentify.sample.mvc.NestedSampleController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider

class NestedSampleDocs : Documentify() {
    private val api = NestedSampleController()

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controller(api)
        }
    }

    @Test
    fun nestedSampleGetApi() {
        documentation("Nested Sample Get API") {
            information {
                description("this is Nested Sample API description")
                tag("nested")
            }
            requestLine(Method.GET, "/nested-sample")
            responseBody {
                objectField("nestedField", "this is nested field") {
                    field("integerField", "this is nested field integerField")
                    field("stringField", "this is nested field stringField")
                }
                arrayField("listedField", "this is listed field") {
                    field("integerField", "this is listed field integerField")
                    field("stringField", "this is listed field stringField")
                }
            }
        }
    }

    @Test
    fun nestedSamplePostApi() {
        documentation("Nested Sample Post API") {
            information {
                description("this is Nested Sample API description")
                tag("nested")
            }
            requestLine(Method.POST, "/nested-sample")
            requestBody("Nested Sample Post Request") {
                objectField("nestedField", "this is nested field") {
                    field("integerField", "this is nested field integerField", 1)
                    field("stringField", "this is nested field stringField", "nested sample")
                }
                arrayField("listedField", "this is listed field") {
                    field("integerField", "this is nested field integerField", 1)
                    field("stringField", "this is nested field stringField", "nested sample 2")
                }
            }
            responseBody {
                objectField("nestedField", "this is nested field") {
                    field("integerField", "this is nested field integerField")
                    field("stringField", "this is nested field stringField")
                }
                arrayField("listedField", "this is listed field") {
                    field("integerField", "this is listed field integerField")
                    field("stringField", "this is listed field stringField")
                }
            }
        }
    }
}