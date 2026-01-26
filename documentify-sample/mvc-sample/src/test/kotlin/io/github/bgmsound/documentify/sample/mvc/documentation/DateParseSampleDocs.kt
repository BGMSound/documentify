package io.github.bgmsound.documentify.sample.mvc.documentation

import io.github.bgmsound.documentify.mvc.Documentify
import io.github.bgmsound.documentify.sample.mvc.controller.DateParseSampleController
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
    fun dateParseSampleApi() {
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