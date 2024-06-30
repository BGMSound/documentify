package kr.bgmsound.documentify.core

import org.springframework.restdocs.request.ParameterDescriptor

open class Parameter(
    val descriptor: ParameterDescriptor
) : SpecElement {
    override val key: String get() = descriptor.name
    val description: String get() = descriptor.description as String
    override val sample: Any get() = descriptor.sample()
}