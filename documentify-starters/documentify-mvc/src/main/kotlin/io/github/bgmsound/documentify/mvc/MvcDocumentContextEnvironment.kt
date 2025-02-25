package io.github.bgmsound.documentify.mvc

import io.github.bgmsound.documentify.core.environment.AbstractDocumentContextEnvironment
import org.springframework.test.web.servlet.MockMvc

abstract class MvcDocumentContextEnvironment : AbstractDocumentContextEnvironment() {

    abstract fun buildMockMvc(): MockMvc

}