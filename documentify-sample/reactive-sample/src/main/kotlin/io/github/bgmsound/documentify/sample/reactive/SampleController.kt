package io.github.bgmsound.documentify.sample.reactive

import io.github.bgmsound.documentify.sample.reactive.dto.request.SampleRequest
import io.github.bgmsound.documentify.sample.reactive.dto.response.SampleResponse
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/sample")
class SampleController {
    @GetMapping("/{integerField}")
    suspend fun sample(
        @PathVariable("integerField") integerField: Int,
        @RequestParam("stringField") stringField: String
    ): SampleResponse {
        return Mono.just(SampleResponse(integerField, stringField)).awaitFirst()
    }

    @PostMapping
    suspend fun samplePost(
        @RequestBody request: SampleRequest
    ): SampleResponse {
        return Mono.just(SampleResponse(request.integerField, request.stringField)).awaitFirst()
    }

    @PatchMapping
    suspend fun samplePatch(
        @RequestBody request: SampleRequest
    ): SampleResponse {
        return Mono.just(SampleResponse(request.integerField, request.stringField)).awaitFirst()
    }

    @DeleteMapping("{id}")
    suspend fun sampleDelete(@PathVariable("id") id: String) {
    }
}