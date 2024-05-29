package kr.bgmsound.documentify.core

import org.springframework.restdocs.snippet.Snippet

interface APISpec {

    fun build(): List<Snippet>

}