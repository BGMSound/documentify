package io.github.bgmsound.documentify.sample.reactive.controller

import io.github.bgmsound.documentify.sample.reactive.dto.request.SampleRequest
import io.github.bgmsound.documentify.sample.reactive.dto.response.SampleResponse
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/sample")
class SampleController {
    @GetMapping("/{integerField}")
    suspend fun sample(
        @PathVariable("integerField") integerField: Int,
        @RequestParam("stringField") stringField: String
    ): SampleResponse {
        "asfd"
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