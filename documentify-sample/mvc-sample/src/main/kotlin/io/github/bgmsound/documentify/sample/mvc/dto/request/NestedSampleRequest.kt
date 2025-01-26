package io.github.bgmsound.documentify.sample.mvc.dto.request

data class NestedSampleRequest(
    val nestedField: SampleRequest,
    val listedField: List<SampleRequest>
)