package io.github.bgmsound.documentify.core.documentation.element.parameter

import io.github.bgmsound.documentify.core.documentation.element.SpecElement
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Attributes

class QueryParameter(
    descriptor: ParameterDescriptor
) : Parameter(descriptor) {
    companion object {
        fun newQueryParameter(
            key: String,
            description: String,
            sample: String,
            requirement: SpecElement.Requirement
        ): QueryParameter {
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
            return QueryParameter(descriptor)
        }
    }
}