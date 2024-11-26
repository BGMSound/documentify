package io.github.bgmsound.documentify.core.documentation.specification

import io.github.bgmsound.documentify.core.documentation.DocumentableSpec
import io.github.bgmsound.documentify.core.documentation.element.SpecElement.Requirement
import io.github.bgmsound.documentify.core.documentation.element.header.Header

abstract class HeaderSpec(
    protected val headers: MutableList<Header> = mutableListOf(),
) : DocumentableSpec {

    fun headers(): List<Header> = headers

    fun header(header: Header): Header {
        return putHeader(header)
    }

    fun header(path: String, description: String, sample: String): Header {
        val header = Header.newHeader(path, description, sample, Requirement.REQUIRED)
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