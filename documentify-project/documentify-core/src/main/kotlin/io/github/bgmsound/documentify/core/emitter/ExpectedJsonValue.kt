package io.github.bgmsound.documentify.core.emitter

data class ExpectedJsonValue(
    val jsonPath: String,
    val expectedValue: Any
) {
    companion object {
        fun of(path: String, expectedValue: Any) = ExpectedJsonValue(path, expectedValue)
    }
}
