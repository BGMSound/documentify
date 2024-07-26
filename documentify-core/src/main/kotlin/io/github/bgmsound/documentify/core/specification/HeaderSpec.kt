package io.github.bgmsound.documentify.core.specification

import io.github.bgmsound.documentify.core.specification.element.Header
import io.github.bgmsound.documentify.core.specification.element.SpecElement.Type

abstract class HeaderSpec(
    protected val headers: MutableList<Header> = mutableListOf(),
) : APISpec {

    fun headers(): List<Header> = headers

    fun header(header: Header): Header {
        return putHeader(header)
    }

    fun header(path: String, description: String, sample: String): Header {
        val header = Header.newHeader(path, description, sample, Type.REQUIRED)
        return putHeader(header)
    }

    fun optionalHeader(path: String, description: String, sample: String): Header {
        val header = Header.newHeader(path, description, sample, Type.OPTIONAL)
        return putHeader(header)
    }

    private fun putHeader(header: Header): Header {
        headers.add(header)
        return header
    }
}