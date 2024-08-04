package io.github.bgmsound.documentify.core.specification.element

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.bgmsound.documentify.core.specification.RestDocUtil.Companion.SAMPLE_KEY
import org.springframework.restdocs.snippet.AbstractDescriptor
import java.time.LocalDate
import java.time.LocalDateTime

abstract class SpecElement(
    private val descriptor: AbstractDescriptor<*>
) {
    private val objectMapper = ObjectMapper()

    abstract val key: String
    val description get() = descriptor.description as String
    val sample: Any get() {
        return if (descriptor.sample() is LocalDate || descriptor.sample() is LocalDateTime) {
            objectMapper.writeValueAsString(descriptor.sample())
        } else {
            descriptor.sample()
        }
    }

    fun hasSample(): Boolean {
        return descriptor.attributes.containsKey(SAMPLE_KEY)
    }

    private fun AbstractDescriptor<*>.sample(): Any {
        return attributes[SAMPLE_KEY] ?: throw IllegalArgumentException("sample not found")
    }

    enum class Requirement {
        REQUIRED,
        OPTIONAL,
        IGNORED;
    }
}