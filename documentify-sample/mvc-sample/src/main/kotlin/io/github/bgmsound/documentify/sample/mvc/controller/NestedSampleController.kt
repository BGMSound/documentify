package io.github.bgmsound.documentify.sample.mvc.controller

import io.github.bgmsound.documentify.sample.mvc.dto.request.NestedSampleRequest
import io.github.bgmsound.documentify.sample.mvc.dto.response.NestedSampleResponse
import io.github.bgmsound.documentify.sample.mvc.dto.response.SampleResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/nested-sample")
class NestedSampleController {
    @GetMapping
    fun nestedSample(): NestedSampleResponse {
        return NestedSampleResponse(
            SampleResponse(1, "nested sample"),
            listOf(SampleResponse(2, "nested sample 2"))
        )
    }

    @PostMapping
    fun nestedSamplePost(@RequestBody request: NestedSampleRequest): NestedSampleResponse {
        return NestedSampleResponse(
            SampleResponse(
                request.nestedField.integerField,
                request.nestedField.stringField
            ),
            listOf(
                SampleResponse(
                    request.listedField[0].integerField,
                    request.listedField[0].stringField
                )
            )
        )
    }
}