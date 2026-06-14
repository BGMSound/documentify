package io.github.bgmsound.documentify.sample.mvc.documentation

import io.github.bgmsound.documentify.core.specification.schema.Method
import io.github.bgmsound.documentify.mvc.Documentify
import io.github.bgmsound.documentify.sample.mvc.controller.UseCase
import io.github.bgmsound.documentify.sample.mvc.controller.UseCaseBindingController
import io.github.bgmsound.documentify.sample.mvc.dto.response.SampleResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.RestDocumentationContextProvider

class UseCaseBindingDocs : Documentify by Documentify.new() {
    private val useCase = mockk<UseCase>()
    private val api = UseCaseBindingController(useCase)

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        standalone(provider) {
            controller(api)
        }
    }

    @Test
    fun useCaseBinding() {
        every { useCase.doSomethingAndReturn() } returns SampleResponse(1, "usecase binding!")
        documentation("Use Case Binding") {
            information {
                description("this is Use Case Binding description")
                tag("use-case")
            }
            requestLine(Method.GET, "/use-case")
            responseBody {
                field("integerField", "this is integerField", 1)
                field("stringField", "this is stringField", "usecase binding!")
            }
        }
    }
}