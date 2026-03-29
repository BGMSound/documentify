package io.github.bgmsound.documentify.core.environment

import org.springframework.restdocs.operation.preprocess.OperationPreprocessor
import org.springframework.test.web.reactive.server.WebTestClient

interface DocumentContextEnvironment {

    fun requestPreprocessors(vararg preprocessors: OperationPreprocessor)

    fun responsePreprocessors(vararg preprocessors: OperationPreprocessor)

    fun requestPreprocessors(preprocessors: List<OperationPreprocessor>) {
        requestPreprocessors(*preprocessors.toTypedArray())
    }

    fun responsePreprocessors(preprocessors: List<OperationPreprocessor>) {
        responsePreprocessors(*preprocessors.toTypedArray())
    }

    fun requestPreprocessors(): List<OperationPreprocessor>

    fun responsePreprocessors(): List<OperationPreprocessor>

    fun buildTestClient(): WebTestClient

}