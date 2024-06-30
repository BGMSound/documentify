package kr.bgmsound.documentify.core.specification.element

import org.springframework.restdocs.headers.HeaderDescriptor

class Header(
    val descriptor: HeaderDescriptor
) : SpecElement {

    override val key: String get() = descriptor.name
    val description get() = descriptor.description as String
    override val sample get() = descriptor.sample()

    enum class Type {
        REQUIRED,
        OPTIONAL;
    }
}
