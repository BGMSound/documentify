package io.github.bgmsound.documentify.core.emitter

import io.github.bgmsound.documentify.core.specification.element.SpecElement

interface DocumentSpecSampleAggregator {

    fun <T : SpecElement> aggregate(specElements: List<T>) : Map<String, Any>

}