package io.github.bgmsound.documentify.core.emitter

data class JsonResultMatcher(
    val jsonPath: String,
    val expectedValue: Any
) {
    companion object {
        fun of(path: String, expectedValue: Any) = JsonResultMatcher(path, expectedValue)
    }
}
