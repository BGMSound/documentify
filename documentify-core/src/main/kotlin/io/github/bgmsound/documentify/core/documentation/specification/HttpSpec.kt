package io.github.bgmsound.documentify.core.documentation.specification

import io.github.bgmsound.documentify.core.documentation.DocumentableSpec

abstract class HttpSpec(
    private val header: HeaderSpec,
    private val body: BodySpec
) : DocumentableSpec {
    val headers get() = header.headers()
    val fields get() = body.fields()
    val schema get() = body.schema()
}