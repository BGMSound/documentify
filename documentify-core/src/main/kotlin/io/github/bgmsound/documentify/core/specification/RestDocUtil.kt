package io.github.bgmsound.documentify.core.specification

class RestDocUtil  {
    companion object {
        const val SAMPLE_KEY = "sample"

        fun Class<*>.isPrimitiveOrWrapper(): Boolean {
            return isPrimitive || when(this.name) {
                "java.lang.Byte" -> true
                "java.lang.Short" -> true
                "java.lang.Integer" -> true
                "java.lang.Long" -> true
                "java.lang.Double" -> true
                "java.lang.Float" -> true
                "java.lang.Boolean" -> true
                "java.lang.Char" -> true
                else -> false
            }
        }
    }
}