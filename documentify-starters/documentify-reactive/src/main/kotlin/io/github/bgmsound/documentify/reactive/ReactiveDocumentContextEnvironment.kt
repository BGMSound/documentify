package io.github.bgmsound.documentify.reactive

import io.github.bgmsound.documentify.core.environment.AbstractDocumentContextEnvironment
import org.springframework.test.web.reactive.server.WebTestClient

abstract class ReactiveDocumentContextEnvironment : AbstractDocumentContextEnvironment() {

    abstract fun buildWebTestClient(): WebTestClient

}