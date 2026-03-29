package io.github.bgmsound.documentify.core.emitter

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/**")
class AlternativeResponseDocumentController private constructor(
    status: Int,
    response: Any
) {
    private val response = response as? ResponseEntity<*> ?: ResponseEntity.status(status).body(response)

    @GetMapping
    fun get(): Any {
        return response
    }

    @PostMapping
    fun post(): Any {
        return response
    }

    @PutMapping
    fun put(): Any {
        return response
    }

    @DeleteMapping
    fun delete(): Any {
        return response
    }

    @PatchMapping
    fun patch(): Any {
        return response
    }

    companion object {
        fun new(
            status: Int,
            response: Any
        ): AlternativeResponseDocumentController {
            return AlternativeResponseDocumentController(status, response)
        }
    }
}