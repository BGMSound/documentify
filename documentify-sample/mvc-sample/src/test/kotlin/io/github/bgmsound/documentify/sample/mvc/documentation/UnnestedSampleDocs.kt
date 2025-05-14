package io.github.bgmsound.documentify.sample.mvc.documentation

import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.mvc.Documentify
import io.github.bgmsound.documentify.sample.mvc.controller.UnnestedSampleController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider

class UnnestedSampleDocs : Documentify() {
    private val api = UnnestedSampleController()

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controller(api)
        }
    }

    @Test
    fun unnestedSampleGetApi() {
        documentation("Unnested Sample Get API") {
            information {
                description("this is Unnested Sample API description")
                tag("unnested")
            }
            requestLine(Method.GET, "/unnested-sample")
        }
    }

    @Test
    fun unnestedListSampleGetApi() {
        documentation("Unnested List Sample Get API") {
            information {
                description("this is Unnested List Sample API description")
                tag("unnested")
            }
            requestLine(Method.GET, "/unnested-sample/list")
        }
    }
}