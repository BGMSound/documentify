package io.github.bgmsound.documentify.core.specification

import io.github.bgmsound.documentify.core.specification.element.Header
import io.github.bgmsound.documentify.core.specification.element.SpecElement.Requirement

abstract class HeaderSpec(
    protected val headers: MutableList<Header> = mutableListOf(),
) : APISpec {

    fun headers(): List<Header> = headers

    fun header(header: Header): Header {
        return putHeader(header)
    }

    fun header(path: String, description: String, sample: String): Header {
        val header = Header.newHeader(path, description, sample, Requirement.REQUIRED)
        return putHeader(header)
    }

    fun bearer(path: String, description: String, sample: String): Header {
        val header = Header.newHeader(
            path,
            description,
            "Bearer $sample",
            Requirement.REQUIRED
        )
        return putHeader(header)
    }

    fun bearer(description: String, sample: String): Header {
        val header = Header.newHeader(
            "Authorization",
            description,
            "Bearer $sample",
            Requirement.REQUIRED
        )
        return putHeader(header)
    }

    fun optionalHeader(path: String, description: String, sample: String): Header {
        val header = Header.newHeader(path, description, sample, Requirement.OPTIONAL)
        return putHeader(header)
    }

    private fun putHeader(header: Header): Header {
        headers.add(header)
        return header
    }
}