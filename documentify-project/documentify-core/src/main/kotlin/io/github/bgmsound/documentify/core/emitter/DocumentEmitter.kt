package io.github.bgmsound.documentify.core.emitter

import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec

interface DocumentEmitter {

    fun emit(): BodyContentSpec

}