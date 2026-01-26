package io.github.bgmsound.documentify.sample.reactive.documentation

import io.github.bgmsound.documentify.reactive.Documentify
import io.github.bgmsound.documentify.sample.reactive.controller.DateParseSampleController
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider

class DateParseSampleDocs : Documentify() {
    private val api = DateParseSampleController()

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controller(api)
        }
    }

    @Test
    fun dateParseSampleApi() = runTest {
        documentation("Date Parse Sample API") {
            information {
                description("this is Date Parse Sample API description")
                tag("date-parse")
            }
            requestLine(io.github.bgmsound.documentify.core.specification.schema.Method.GET, "/date-parse-sample") {
                queryParameter("time", "time", "2024-01-01 12:00:00")
            }
        }
    }
}