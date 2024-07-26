package io.github.bgmsound.documentify.core.specification

import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.SAMPLE_KEY
import io.github.bgmsound.documentify.core.specification.element.Header
import io.github.bgmsound.documentify.core.specification.element.Header.Type
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.snippet.Attributes

abstract class HeaderSpec(
    protected val headers: MutableList<Header> = mutableListOf()
) : APISpec {

    fun headers(): List<Header> = headers

    fun header(header: Header) {
        headers.add(header)
    }

    fun header(path: String, description: String, sample: String) {
        header(path, description, sample, Type.REQUIRED)
    }

    fun optionalHeader(path: String, description: String, sample: String) {
        header(path, description, sample, Type.OPTIONAL)
    }

    fun header(
        key: String,
        sample: String,
        description: String,
        type: Type
    ) {
        val descriptor = HeaderDocumentation.headerWithName(key)
            .description(description)
            .attributes(
                Attributes.Attribute(SAMPLE_KEY, sample)
            )
        when (type) {
            Type.REQUIRED -> {}
            Type.OPTIONAL -> descriptor.optional()
        }
        val header = Header(descriptor)
        this.header(header)
    }
}