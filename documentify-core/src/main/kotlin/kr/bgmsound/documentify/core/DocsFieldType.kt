package kr.bgmsound.documentify.core

import org.springframework.restdocs.payload.JsonFieldType
import kotlin.reflect.KClass

sealed class DocsFieldType(val type: JsonFieldType)

data object ARRAY : DocsFieldType(JsonFieldType.ARRAY)
data object BOOLEAN : DocsFieldType(JsonFieldType.BOOLEAN)
data object NUMBER : DocsFieldType(JsonFieldType.NUMBER)
data object OBJECT : DocsFieldType(JsonFieldType.OBJECT)
data object STRING : DocsFieldType(JsonFieldType.STRING)
data object ANY: DocsFieldType(JsonFieldType.VARIES)
data object DATE : DocsFieldType(JsonFieldType.STRING)
data object DATETIME : DocsFieldType(JsonFieldType.STRING)
data object NULL : DocsFieldType(JsonFieldType.NULL)
data class ENUM<T : Enum<T>>(val enums: Collection<T>) : DocsFieldType(JsonFieldType.STRING) {
    constructor(clazz: KClass<T>) : this(clazz.java.enumConstants.asList())   // (1)
}