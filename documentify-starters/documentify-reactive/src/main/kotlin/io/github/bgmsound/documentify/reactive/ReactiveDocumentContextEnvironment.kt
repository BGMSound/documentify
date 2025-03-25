package io.github.bgmsound.documentify.reactive

import io.github.bgmsound.documentify.core.environment.DocumentContextEnvironment
import org.springframework.test.web.reactive.server.WebTestClient

interface ReactiveDocumentContextEnvironment : DocumentContextEnvironment {

    fun buildWebTestClient(): WebTestClient

}