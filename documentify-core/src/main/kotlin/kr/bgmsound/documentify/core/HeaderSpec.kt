package kr.bgmsound.documentify.core

import kr.bgmsound.documentify.core.Header.Type
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.snippet.Attributes

abstract class HeaderSpec(
    protected val headers: MutableList<Header> = mutableListOf()
) : APISpec {

    fun headers(): Map<String, String> = headers.of()

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
                Attributes.Attribute(SAMPLE, sample)
            )
        when (type) {
            Type.REQUIRED -> {}
            Type.OPTIONAL -> descriptor.optional()
        }
        val header = Header(descriptor)
        this.header(header)
    }

    private fun List<Header>.of(): Map<String, String> {
        return associate { it.key to it.sample }
    }
}