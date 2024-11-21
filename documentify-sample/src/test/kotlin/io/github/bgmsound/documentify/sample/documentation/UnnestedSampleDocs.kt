package io.github.bgmsound.documentify.sample.documentation

import io.github.bgmsound.documentify.core.Documentify
import io.github.bgmsound.documentify.sample.UnnestedSampleController
import io.restassured.http.Method
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