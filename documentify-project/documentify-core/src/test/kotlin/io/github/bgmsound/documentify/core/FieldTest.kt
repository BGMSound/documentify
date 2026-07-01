package io.github.bgmsound.documentify.core

import io.github.bgmsound.documentify.core.specification.element.field.STRING
import io.github.bgmsound.documentify.core.specification.schema.response.ResponseBodySpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.JsonFieldType
import java.time.LocalDate
import java.time.LocalDateTime

class FieldTest {

    @Test
    fun `top level array sample is detected as array`() {
        val tags = ResponseBodySpec().field("tags", "tags", arrayOf("a", "b"))

        assertThat(tags.isArray()).isTrue()
    }

    @Test
    fun `nested list sample is detected as array`() {
        val body = ResponseBodySpec().apply {
            objectField("outer", "outer") {
                field("tags", "tags", listOf("a", "b"))
            }
        }
        val tags = body.fields().single().childFields().single()

        assertThat(tags.isArray()).isTrue()
    }

    @Test
    fun `nested array sample is detected as array`() {
        val body = ResponseBodySpec().apply {
            objectField("outer", "outer") {
                field("tags", "tags", arrayOf("a", "b"))
            }
        }
        val tags = body.fields().single().childFields().single()

        assertThat(tags.isArray()).isTrue()
    }

    @Test
    fun `boolean sample is detected as boolean`() {
        val flag = ResponseBodySpec().field("flag", "flag", true)

        assertThat(flag.build().single().type).isEqualTo(JsonFieldType.BOOLEAN)
    }

    @Test
    fun `number sample is detected as number`() {
        val count = ResponseBodySpec().field("count", "count", 42)

        assertThat(count.build().single().type).isEqualTo(JsonFieldType.NUMBER)
    }

    @Test
    fun `local date sample is detected as string`() {
        val date = ResponseBodySpec().field("date", "date", LocalDate.of(2024, 1, 1))

        assertThat(date.build().single().type).isEqualTo(JsonFieldType.STRING)
    }

    @Test
    fun `local date time sample is detected as string`() {
        val dateTime = ResponseBodySpec().field("dateTime", "dateTime", LocalDateTime.of(2024, 1, 1, 0, 0))

        assertThat(dateTime.build().single().type).isEqualTo(JsonFieldType.STRING)
    }

    @Test
    fun `enum sample is detected as string`() {
        val color = ResponseBodySpec().field("color", "color", Color.RED)

        assertThat(color.build().single().type).isEqualTo(JsonFieldType.STRING)
    }

    @Test
    fun `local date sample serializes to iso string without quotes`() {
        val field = ResponseBodySpec().field("date", "date", LocalDate.of(2024, 1, 1))

        assertThat(field.sample).isEqualTo("2024-01-01")
    }

    @Test
    fun `local date time sample serializes to iso string without quotes`() {
        val field = ResponseBodySpec().field("dateTime", "dateTime", LocalDateTime.of(2024, 1, 1, 12, 30, 45))

        assertThat(field.sample).isEqualTo("2024-01-01T12:30:45")
    }

    @Test
    fun `adding a child to a scalar field throws`() {
        val name = ResponseBodySpec().field("name", "name", "John")

        assertThatThrownBy { name.field("child", "child", "x") }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `changing a populated container to a scalar type throws`() {
        val outer = ResponseBodySpec().objectField("outer", "outer") {
            field("inner", "inner", "x")
        }

        assertThatThrownBy { outer.type(STRING) }
            .isInstanceOf(IllegalStateException::class.java)
    }

    private enum class Color { RED, BLUE }
}
