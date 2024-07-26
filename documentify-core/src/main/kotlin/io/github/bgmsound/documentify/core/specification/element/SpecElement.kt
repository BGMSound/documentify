package io.github.bgmsound.documentify.core.specification.element

import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.SAMPLE_KEY
import org.springframework.restdocs.snippet.AbstractDescriptor

abstract class SpecElement(
    private val descriptor: AbstractDescriptor<*>
) {
    abstract val key: String
    val description get() = descriptor.description as String
    val sample get() = descriptor.sample()

    private fun AbstractDescriptor<*>.sample(): Any {
        return attributes[SAMPLE_KEY] ?: throw IllegalArgumentException("sample not found")
    }
}