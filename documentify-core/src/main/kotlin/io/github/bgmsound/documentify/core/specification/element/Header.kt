package io.github.bgmsound.documentify.core.specification.element

import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.SAMPLE_KEY
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.snippet.Attributes

class Header(
    val descriptor: HeaderDescriptor,
) : SpecElement(descriptor) {

    override val key: String get() = descriptor.name

    companion object {
        fun newHeader(
            key: String,
            sample: String,
            description: String,
            type: Type
        ): Header {
            val descriptor = HeaderDocumentation.headerWithName(key)
                .description(description)
                .attributes(
                    Attributes.Attribute(SAMPLE_KEY, sample)
                )
            when (type) {
                Type.REQUIRED -> {}
                Type.OPTIONAL -> descriptor.optional()
                else -> throw IllegalArgumentException("Header type must be REQUIRED or OPTIONAL")
            }
            return Header(descriptor)
        }
    }
}
