package io.github.bgmsound.documentify.core.documentation

import org.springframework.restdocs.snippet.Snippet

interface DocumentableSpec {

    fun build(): List<Snippet>

}