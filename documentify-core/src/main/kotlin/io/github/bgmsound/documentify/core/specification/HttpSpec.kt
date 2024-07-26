package io.github.bgmsound.documentify.core.specification

abstract class HttpSpec(
    private val header: HeaderSpec,
    private val body: BodySpec
) : APISpec {
    val headers get() = header.headers()
    val fields get() = body.fields()
    val schema get() = body.schema()
}