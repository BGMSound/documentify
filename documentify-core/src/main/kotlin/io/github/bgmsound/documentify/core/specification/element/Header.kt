package io.github.bgmsound.documentify.core.specification.element

import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.SAMPLE_KEY
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.snippet.Attributes

class Header(
    private val descriptor: HeaderDescriptor,
) : SpecElement(descriptor) {
    override val key: String get() = descriptor.name

    fun build() = descriptor

    companion object {
        fun newHeader(
            key: String,
            sample: String,
            description: String,
            requirement: Requirement
        ): Header {
            val descriptor = HeaderDocumentation.headerWithName(key)
                .description(description)
                .attributes(
                    Attributes.Attribute(SAMPLE_KEY, sample)
                )
            when (requirement) {
                Requirement.REQUIRED -> {}
                Requirement.OPTIONAL -> descriptor.optional()
                else -> throw IllegalArgumentException("Header type must be REQUIRED or OPTIONAL")
            }
            return Header(descriptor)
        }
    }
}
