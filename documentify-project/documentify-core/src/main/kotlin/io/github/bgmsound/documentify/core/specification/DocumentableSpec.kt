package io.github.bgmsound.documentify.core.specification

import org.springframework.restdocs.snippet.Snippet

interface DocumentableSpec {

    fun build(): List<Snippet>

}