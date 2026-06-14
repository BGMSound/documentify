package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.emitter.DefaultDocumentSpecSampleAggregator
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseBodySpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class SpecElementAggregatorTest {

    private fun ResponseBodySpec.aggregated(): Map<String, Any> =
        DefaultDocumentSpecSampleAggregator.aggregate(fields())

    @Test
    fun `flat scalar fields are aggregated as a flat map`() {
        val result = ResponseBodySpec().apply {
            field("id", "id", 1)
            field("name", "name", "John")
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("id" to 1, "name" to "John"))
    }

    @Test
    fun `nested object dsl is aggregated as a nested map`() {
        val result = ResponseBodySpec().apply {
            objectField("user", "user") {
                field("name", "name", "John")
                field("age", "age", 30)
            }
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("user" to mapOf("name" to "John", "age" to 30)))
    }

    @Test
    fun `nested array dsl is aggregated as a single element list`() {
        val result = ResponseBodySpec().apply {
            arrayField("items", "items") {
                field("id", "id", 1)
                field("label", "label", "first")
            }
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("items" to listOf(mapOf("id" to 1, "label" to "first"))))
    }

    @Test
    fun `optional field with sample is included`() {
        val result = ResponseBodySpec().apply {
            field("id", "id", 1)
            optionalField("nickname", "nickname", "JJ")
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("id" to 1, "nickname" to "JJ"))
    }

    @Test
    fun `ignored field with sample is excluded`() {
        val result = ResponseBodySpec().apply {
            field("id", "id", 1)
            ignoreField("secret", "secret", "hidden-value")
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("id" to 1))
    }

    @Test
    fun `nested ignored field is excluded`() {
        val result = ResponseBodySpec().apply {
            objectField("user", "user") {
                field("name", "name", "John")
                ignoreField("password", "password")
            }
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("user" to mapOf("name" to "John")))
    }

    @Test
    fun `flat jsonpath builds nested structure`() {
        val result = ResponseBodySpec().apply {
            field("user.name", "name", "John")
            field("user.age", "age", 30)
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("user" to mapOf("name" to "John", "age" to 30)))
    }

    @Test
    fun `flat jsonpath array notation builds list structure`() {
        val result = ResponseBodySpec().apply {
            field("items[].id", "id", 1)
            field("items[].label", "label", "first")
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("items" to listOf(mapOf("id" to 1, "label" to "first"))))
    }

    @Test
    fun `flat and nested fields for the same object are merged`() {
        val result = ResponseBodySpec().apply {
            field("user.name", "name", "John")
            objectField("user", "user") {
                field("age", "age", 30)
            }
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("user" to mapOf("name" to "John", "age" to 30)))
    }

    @Test
    fun `field declared as both value and parent throws`() {
        val body = ResponseBodySpec().apply {
            field("user", "user", "literal")
            field("user.name", "name", "John")
        }

        assertThatThrownBy { body.aggregated() }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `field without sample throws`() {
        val body = ResponseBodySpec().apply {
            field("name", "name")
        }

        assertThatThrownBy { body.aggregated() }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `bracket quoted key with a dot is treated as a single key`() {
        val result = ResponseBodySpec().apply {
            field("data['a.b']", "a.b", "value")
        }.aggregated()

        assertThat(result).isEqualTo(mapOf("data" to mapOf("a.b" to "value")))
    }

    @Test
    fun `indexed array notation builds a multi element list`() {
        val result = ResponseBodySpec().apply {
            field("items[0].id", "id", 1)
            field("items[0].name", "name", "a")
            field("items[1].id", "id", 2)
            field("items[1].name", "name", "b")
        }.aggregated()

        assertThat(result).isEqualTo(
            mapOf(
                "items" to listOf(
                    mapOf("id" to 1, "name" to "a"),
                    mapOf("id" to 2, "name" to "b"),
                )
            )
        )
    }

    @Test
    fun `empty bracket and zero index collapse to the same element`() {
        val result = ResponseBodySpec().apply {
            field("items[].id", "id", 1)
            field("items[0].name", "name", "a")
        }.aggregated()

        assertThat(result).isEqualTo(
            mapOf("items" to listOf(mapOf("id" to 1, "name" to "a")))
        )
    }
}
