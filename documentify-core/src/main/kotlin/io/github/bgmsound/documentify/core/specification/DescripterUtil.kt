package io.github.bgmsound.documentify.core.specification

import org.springframework.restdocs.snippet.AbstractDescriptor

const val SAMPLE_KEY = "sample"

fun AbstractDescriptor<*>.sample(): Any {
    return attributes[SAMPLE_KEY] ?: throw IllegalArgumentException("sample not found")
}