package io.github.bgmsound.documentify.reactive.emitter

import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec

interface ReactiveDocumentEmitter {

    suspend fun emit(): BodyContentSpec

}