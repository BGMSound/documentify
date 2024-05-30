package kr.bgmsound.documentify.core

import org.springframework.restdocs.request.ParameterDescriptor

data class PathVariable(
    val descriptor: ParameterDescriptor
) {
    val key get() = descriptor.name
    val description get() = descriptor.description as String
    val sample get() = descriptor.sample() as String
}