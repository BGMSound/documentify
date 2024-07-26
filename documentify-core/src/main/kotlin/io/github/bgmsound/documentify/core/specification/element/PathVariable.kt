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
            type: Type
        ): PathVariable {
            val descriptor = RequestDocumentation.parameterWithName(key)
                .description(description)
                .attributes(
                    Attributes.Attribute(SAMPLE_KEY, sample)
                )
            when (type) {
                Type.REQUIRED -> {}
                Type.OPTIONAL -> descriptor.optional()
                Type.IGNORED -> descriptor.ignored()
            }
            return PathVariable(descriptor)
        }
    }
}