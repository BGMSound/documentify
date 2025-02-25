package io.github.bgmsound.documentify.core.environment

import org.springframework.restdocs.operation.preprocess.OperationPreprocessor

abstract class AbstractDocumentContextEnvironment : DocumentContextEnvironment {
    private val requestPreprocessors = mutableListOf<OperationPreprocessor>()
    private val responsePreprocessors = mutableListOf<OperationPreprocessor>()

    override fun requestPreprocessors(vararg preprocessors: OperationPreprocessor) {
        requestPreprocessors.addAll(preprocessors)
    }

    override fun responsePreprocessors(vararg preprocessors: OperationPreprocessor) {
        responsePreprocessors.addAll(preprocessors)
    }

    override fun requestPreprocessors(): List<OperationPreprocessor> {
        return requestPreprocessors
    }

    override fun responsePreprocessors(): List<OperationPreprocessor> {
        return responsePreprocessors
    }
}