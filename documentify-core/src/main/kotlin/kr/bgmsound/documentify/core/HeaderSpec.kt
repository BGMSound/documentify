package kr.bgmsound.documentify.core

import kr.bgmsound.documentify.core.Header.Type
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.snippet.Attributes

abstract class HeaderSpec(
    protected val headers: MutableList<Header> = mutableListOf()
) : APISpec {

    fun header(header: Header) {
        headers.add(header)
    }

    fun header(path: String, description: String, sample: String) {
        header(path, description, sample, Type.REQUIRED)
    }

    fun optionalHeader(path: String, description: String, sample: String) {
        header(path, description, sample, Type.REQUIRED)
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
                Attributes.Attribute("sample", sample)
            )
        when (type) {
            Type.REQUIRED -> {}
            Type.OPTIONAL -> descriptor.optional()
        }
        val header = Header(descriptor)
        this.header(header)
    }
}