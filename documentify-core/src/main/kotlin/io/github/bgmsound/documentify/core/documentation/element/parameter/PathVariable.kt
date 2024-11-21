package io.github.bgmsound.documentify.core.documentation.element.parameter

import io.github.bgmsound.documentify.core.documentation.element.SpecElement
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
            requirement: SpecElement.Requirement
        ): PathVariable {
            val descriptor = RequestDocumentation.parameterWithName(key)
                .description(description)
                .attributes(
                    Attributes.Attribute(SpecElement.SAMPLE_KEY, sample)
                )
            when (requirement) {
                SpecElement.Requirement.REQUIRED -> {}
                SpecElement.Requirement.OPTIONAL -> descriptor.optional()
                SpecElement.Requirement.IGNORED -> descriptor.ignored()
            }
            return PathVariable(descriptor)
        }
    }
}