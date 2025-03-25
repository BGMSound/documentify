package io.github.bgmsound.documentify.mvc

import io.github.bgmsound.documentify.core.environment.DocumentContextEnvironment
import org.springframework.test.web.servlet.MockMvc

interface MvcDocumentContextEnvironment : DocumentContextEnvironment {

    fun buildMockMvc(): MockMvc

}