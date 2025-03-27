package io.github.bgmsound.documentify.core.environment

import org.springframework.restdocs.operation.preprocess.OperationPreprocessor

interface DocumentContextEnvironment {

    fun requestPreprocessors(vararg preprocessors: OperationPreprocessor)

    fun responsePreprocessors(vararg preprocessors: OperationPreprocessor)

    fun requestPreprocessors(preprocessors: List<OperationPreprocessor>) {
        requestPreprocessors(*preprocessors.toTypedArray())
    }

    fun responsePreprocessors(preprocessors: List<OperationPreprocessor>) {
        requestPreprocessors(*preprocessors.toTypedArray())
    }

    fun requestPreprocessors(): List<OperationPreprocessor>

    fun responsePreprocessors(): List<OperationPreprocessor>

}