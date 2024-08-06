package io.github.bgmsound.documentify.sample.dto.request

data class NestedSampleRequest(
    val nestedField: SampleRequest,
    val listedField: List<SampleRequest>
)