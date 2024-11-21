package io.github.bgmsound.documentify.core.documentation

import org.springframework.restdocs.snippet.Snippet

interface APISpec {

    fun build(): List<Snippet>

}