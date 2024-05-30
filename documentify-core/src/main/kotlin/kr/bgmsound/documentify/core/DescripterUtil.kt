package kr.bgmsound.documentify.core

import org.springframework.restdocs.snippet.AbstractDescriptor

const val SAMPLE = "sample"

fun AbstractDescriptor<*>.sample(): Any {
    return attributes[SAMPLE] ?: throw IllegalArgumentException("sample not found")
}