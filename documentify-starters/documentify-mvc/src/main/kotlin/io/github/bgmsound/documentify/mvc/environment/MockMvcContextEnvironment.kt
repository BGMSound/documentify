package io.github.bgmsound.documentify.mvc.environment

import io.github.bgmsound.documentify.mvc.MvcDocumentContextEnvironment
import org.springframework.test.web.servlet.MockMvc

class MockMvcContextEnvironment private constructor(
    private val mockMvc: MockMvc
): MvcDocumentContextEnvironment() {
    override fun buildMockMvc(): MockMvc {
        return mockMvc
    }

    companion object {
        fun mockMvcEnvironment(mockMvc: MockMvc): MockMvcContextEnvironment {
            return MockMvcContextEnvironment(mockMvc)
        }
    }
}