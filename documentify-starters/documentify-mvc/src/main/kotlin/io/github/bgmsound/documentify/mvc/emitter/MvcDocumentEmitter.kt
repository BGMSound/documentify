package io.github.bgmsound.documentify.mvc.emitter

import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse

interface MvcDocumentEmitter {

    fun emit(): ValidatableMockMvcResponse

}