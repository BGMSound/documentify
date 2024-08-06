package io.github.bgmsound.documentify.sample

import io.github.bgmsound.documentify.sample.dto.request.SampleRequest
import io.github.bgmsound.documentify.sample.dto.response.SampleResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
}