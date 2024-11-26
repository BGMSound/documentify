package io.github.bgmsound.documentify.core.documentation.element

import org.springframework.restdocs.hypermedia.HypermediaDocumentation
import org.springframework.restdocs.hypermedia.LinkDescriptor

class Link (
    private val descriptor: LinkDescriptor,
): SpecElement(descriptor) {
    override val key: String get() = descriptor.rel

    fun build() = descriptor

    fun optional() {
        descriptor.optional()
    }

    companion object {
        fun newLink(
            rel: String,
            description: String,
            requirement: Requirement = Requirement.REQUIRED
        ): Link {
            val descriptor = HypermediaDocumentation
                .linkWithRel(rel)
                .description(description)
            when (requirement) {
                Requirement.REQUIRED -> {}
                Requirement.OPTIONAL -> descriptor.ignored()
                else -> throw IllegalArgumentException("Link must be required or optional")
            }
            return Link(descriptor)
        }

        fun newLink(
            rel: String
        ): Link {
            val descriptor = HypermediaDocumentation.linkWithRel(rel)
            return Link(descriptor)
        }
    }
}