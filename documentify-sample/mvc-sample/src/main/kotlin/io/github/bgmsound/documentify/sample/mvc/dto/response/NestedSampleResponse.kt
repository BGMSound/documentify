package io.github.bgmsound.documentify.sample.mvc.dto.response

data class NestedSampleResponse(
    val nestedField: SampleResponse,
    val listedField: List<SampleResponse>
)