package io.github.bgmsound.documentify.sample

import io.github.bgmsound.documentify.sample.dto.response.ComplexSampleResponse
import io.github.bgmsound.documentify.sample.dto.response.NestedSampleResponse
import io.github.bgmsound.documentify.sample.dto.response.SampleResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/complex-sample")
class ComplexSampleController {
    @GetMapping
    fun complexSample(): ComplexSampleResponse {
        return ComplexSampleResponse(
            NestedSampleResponse(
                SampleResponse(1, "nested sample"),
                listOf(SampleResponse(2, "nested sample 2"))
            )
        )
    }
}