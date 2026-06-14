package io.github.bgmsound.documentify.core.specification

import org.springframework.restdocs.snippet.Snippet

@DocumentifyDsl
interface DocumentableSpec {

    fun build(): List<Snippet>

}