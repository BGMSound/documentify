package io.github.bgmsound.documentify.sample.dto.response

data class NestedSampleResponse(
    val nestedField: SampleResponse,
    val listedField: List<SampleResponse>
)