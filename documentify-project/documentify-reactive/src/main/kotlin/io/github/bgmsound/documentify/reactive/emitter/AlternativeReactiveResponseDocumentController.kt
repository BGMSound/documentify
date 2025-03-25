package io.github.bgmsound.documentify.reactive.emitter

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/**")
class AlternativeReactiveResponseDocumentController private constructor(
    private val status: Int,
    private val response: Any
) {
    @GetMapping
    fun get(): Mono<ResponseEntity<*>> {
        return response()
    }

    @PostMapping
    fun post(): Mono<ResponseEntity<*>> {
        return response()
    }

    @PutMapping
    fun put(): Mono<ResponseEntity<*>> {
        return response()
    }

    @DeleteMapping
    fun delete(): Mono<ResponseEntity<*>> {
        return response()
    }

    @PatchMapping
    fun patch(): Mono<ResponseEntity<*>> {
        return response()
    }

    private fun response(): Mono<ResponseEntity<*>> {
        if (response is ResponseEntity<*>) {
            return Mono.just(response)
        }
        return Mono.just(ResponseEntity.status(status).body(response))
    }

    companion object {
        fun new(
            status: Int,
            response: Any
        ): AlternativeReactiveResponseDocumentController {
            return AlternativeReactiveResponseDocumentController(status, response)
        }
    }
}