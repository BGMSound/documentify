package kr.bgmsound.documentify.core

import org.springframework.restdocs.request.ParameterDescriptor

open class Parameter(
    val descriptor: ParameterDescriptor
) {
    val key: String get() = descriptor.name
    val description: String get() = descriptor.description as String
    val sample: String get() = descriptor.sample() as String
}