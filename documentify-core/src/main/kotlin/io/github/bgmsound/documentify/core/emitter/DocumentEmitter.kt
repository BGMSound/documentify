package io.github.bgmsound.documentify.core.emitter

import org.springframework.test.web.servlet.MockMvc

interface DocumentEmitter {

    fun emit(mockMvc: MockMvc)

}