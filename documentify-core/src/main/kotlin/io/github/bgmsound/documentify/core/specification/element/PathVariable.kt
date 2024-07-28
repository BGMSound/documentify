package io.github.bgmsound.documentify.core.specification.element

import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.SAMPLE_KEY
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Attributes

class PathVariable(
    descriptor: ParameterDescriptor
) : Parameter(descriptor) {
    companion object {
        fun newPathVariable(
            key: String,
            description: String,
            sample: String,
            requirement: Requirement
        ): PathVariable {
            val descriptor = RequestDocumentation.parameterWithName(key)
                .description(description)
                .attributes(
                    Attributes.Attribute(SAMPLE_KEY, sample)
                )
            when (requirement) {
                Requirement.REQUIRED -> {}
                Requirement.OPTIONAL -> descriptor.optional()
                Requirement.IGNORED -> descriptor.ignored()
            }
            return PathVariable(descriptor)
        }
    }
}