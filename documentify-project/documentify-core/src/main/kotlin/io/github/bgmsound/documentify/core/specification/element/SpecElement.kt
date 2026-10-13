package io.github.bgmsound.documentify.core.specification.element

import org.springframework.restdocs.snippet.AbstractDescriptor
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import java.time.LocalDate
import java.time.LocalDateTime

abstract class SpecElement(
    private val descriptor: AbstractDescriptor<*>
) {
    abstract val key: String
    val description get() = descriptor.description as String
    val sample: Any get() {
        val raw = descriptor.sample()
        return if (raw is LocalDate || raw is LocalDateTime) {
            OBJECT_MAPPER.writeValueAsString(raw).removeSurrounding("\"")
        } else {
            raw
        }
    }

    fun hasSample(): Boolean {
        return descriptor.attributes.containsKey(SAMPLE_KEY)
    }

    private fun AbstractDescriptor<*>.sample(): Any {
        return attributes[SAMPLE_KEY] ?: throw IllegalArgumentException("sample not found")
    }

    companion object {
        const val SAMPLE_KEY = "sample"

        private val OBJECT_MAPPER: ObjectMapper = JsonMapper.builder()
            .findAndAddModules()
            .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build()
    }

    enum class Requirement {
        REQUIRED,
        OPTIONAL,
        IGNORED;
    }
}