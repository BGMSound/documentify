package kr.bgmsound.documentify.core

import org.springframework.restdocs.snippet.Snippet

interface Spec {

    fun build(): List<Snippet>

}