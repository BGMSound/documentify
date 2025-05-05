package io.github.bgmsound.documentify.mvc.emitter

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/**")
class AlternativeMvcResponseDocumentController private constructor(
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
        ): AlternativeMvcResponseDocumentController {
            return AlternativeMvcResponseDocumentController(status, response)
        }
    }
}