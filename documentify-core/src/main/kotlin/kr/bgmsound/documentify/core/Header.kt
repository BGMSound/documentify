package kr.bgmsound.documentify.core

import org.springframework.restdocs.headers.HeaderDescriptor

data class Header(
    val descriptor: HeaderDescriptor
) {
    enum class Type {
        REQUIRED,
        OPTIONAL;
    }
}
