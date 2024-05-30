package kr.bgmsound.documentify.core

import org.springframework.restdocs.headers.HeaderDescriptor

data class Header(
    val descriptor: HeaderDescriptor
) {

    val key get() = descriptor.name
    val description get() = descriptor.description as String
    val sample get() = descriptor.attributes[SAMPLE] as String

    enum class Type {
        REQUIRED,
        OPTIONAL;
    }
}
