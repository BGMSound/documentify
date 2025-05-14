package io.github.bgmsound.documentify.sample.mvc.controller

import io.github.bgmsound.documentify.sample.mvc.dto.request.SampleRequest
import io.github.bgmsound.documentify.sample.mvc.dto.response.SampleResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sample")
class SampleController {
    @GetMapping("/{integerField}")
    fun sample(
        @PathVariable("integerField") integerField: Int,
        @RequestParam("stringField") stringField: String
    ): SampleResponse {
        return SampleResponse(integerField, stringField)
    }

    @PostMapping
    fun samplePost(
        @RequestBody request: SampleRequest
    ): SampleResponse {
        return SampleResponse(request.integerField, request.stringField)
    }

    @PatchMapping
    fun samplePatch(
        @RequestBody request: SampleRequest
    ): SampleResponse {
        return SampleResponse(request.integerField, request.stringField)
    }

    @DeleteMapping("{id}")
    fun sampleDelete(@PathVariable("id") id: String) {
    }
}