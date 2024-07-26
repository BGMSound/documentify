package io.github.bgmsound.documentify.core.specification.element

import org.springframework.restdocs.headers.HeaderDescriptor

class Header(
    val descriptor: HeaderDescriptor,
) : SpecElement(descriptor) {

    override val key: String get() = descriptor.name

    enum class Type {
        REQUIRED,
        OPTIONAL;
    }
}
