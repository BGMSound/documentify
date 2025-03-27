package io.github.bgmsound.documentify.core.specification.schema

import io.github.bgmsound.documentify.core.specification.DocumentableSpec

abstract class HttpSpec(
    private val header: HeaderSpec,
    private val body: BodySpec
) : DocumentableSpec {
    val headers get() = header.headers()
    val fields get() = body.fields()
    var schema get() = body.schema()
        set(value) {
            if (value != null) {
                body.schema(value)
            }
        }
}