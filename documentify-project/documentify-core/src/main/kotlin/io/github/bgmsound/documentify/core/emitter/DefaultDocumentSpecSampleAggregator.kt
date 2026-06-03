package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.element.SpecElement
import io.github.bgmsound.documentify.core.specification.element.field.Field

object DefaultDocumentSpecSampleAggregator : DocumentSpecSampleAggregator {
    @Suppress("UNCHECKED_CAST")
    override fun <T : SpecElement> aggregate(specElements: List<T>): Map<String, Any> {
        if (specElements.isEmpty()) {
            return emptyMap()
        }
        if (specElements.any { it !is Field }) {
            return specElements.associate { it.key to it.sample }
        }
        val leaves = collectLeaves(specElements as List<Field>)
        if (leaves.isEmpty()) {
            return emptyMap()
        }
        val entries = leaves.map { (path, sample) ->
            Entry(parsePath(path), sample)
        }
        return assemble(entries) as Map<String, Any>
    }

    private fun collectLeaves(fields: List<Field>): List<Pair<String, Any>> {
        return fields.flatMap { field ->
            when {
                field.isIgnored() -> emptyList()
                field.hasSample() -> listOf(field.path to field.sample)
                field.canHaveChild() -> collectLeaves(field.childFields())
                else -> throw IllegalStateException(
                    "Field '${field.path}' must have a sample or child fields"
                )
            }
        }
    }

    private fun parsePath(path: String): List<PathSegment> {
        return SEGMENT.findAll(path).fold(emptyList()) { segments, match ->
            when {
                match.groups[1] != null -> segments + PathSegment(match.groupValues[1], arrayIndex = null)
                match.groups[2] != null -> if (segments.isEmpty()) {
                    segments
                } else {
                    segments.dropLast(1) + segments.last()
                        .copy(arrayIndex = match.groupValues[2].ifEmpty { "0" }.toInt())
                }

                else -> segments + PathSegment(match.groupValues[3], arrayIndex = null)
            }
        }
    }

    private fun assemble(entries: List<Entry>, prefix: String = ""): Any {
        val terminal = entries.firstOrNull { it.segments.isEmpty() }
        if (terminal != null) {
            check(entries.all { it.segments.isEmpty() }) {
                "Conflicting field declaration at '${prefix.ifEmpty { "<root>" }}': " +
                    "declared as a value and as a parent of nested fields"
            }
            return terminal.sample
        }
        return entries
            .groupBy { it.segments.first().name }
            .mapValues { (name, group) ->
                val childPrefix = if (prefix.isEmpty()) name else "$prefix.$name"
                if (group.first().segments.first().arrayIndex == null) {
                    assemble(group.descend(), childPrefix)
                } else {
                    assembleArray(group, childPrefix)
                }
            }
    }

    private fun assembleArray(entries: List<Entry>, prefix: String): List<Any> =
        entries
            .groupBy {
                it.segments.first().arrayIndex ?: error("Conflicting field declaration at '$prefix': mixed array and object notation")
            }
            .toSortedMap()
            .map { (index, indexed) -> assemble(indexed.descend(), "$prefix[$index]") }

    private fun List<Entry>.descend(): List<Entry> =
        map { Entry(it.segments.drop(1), it.sample) }

    private data class Entry(val segments: List<PathSegment>, val sample: Any)
    private data class PathSegment(val name: String, val arrayIndex: Int?)

    private val SEGMENT = Regex("""\['([^']*)']|\[(\d*)]|([^.\[\]]+)""")
}