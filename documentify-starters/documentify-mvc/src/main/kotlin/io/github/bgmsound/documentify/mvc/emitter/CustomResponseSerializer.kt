package io.github.bgmsound.documentify.mvc.emitter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.restdocs.operation.OperationRequest
import org.springframework.restdocs.operation.OperationRequestFactory
import org.springframework.restdocs.operation.OperationResponse
import org.springframework.restdocs.operation.OperationResponseFactory
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor

class CustomResponseSerializer(
    private val objectMapper: ObjectMapper
): OperationPreprocessor {
    override fun preprocess(request: OperationRequest): OperationRequest {
        return request
    }

    override fun preprocess(response: OperationResponse): OperationResponse {
        if (response.content.isEmpty()) {
            return response
        }
        val content = objectMapper.readValue(response.content, Any::class.java)
        return OperationResponseFactory()
            .createFrom(response, objectMapper.writeValueAsBytes(content))
    }
}