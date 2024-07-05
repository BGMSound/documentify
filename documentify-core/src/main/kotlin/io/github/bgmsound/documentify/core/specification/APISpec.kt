package io.github.bgmsound.documentify.core.specification

import org.springframework.restdocs.snippet.Snippet

interface APISpec {

    fun build(): List<Snippet>

}