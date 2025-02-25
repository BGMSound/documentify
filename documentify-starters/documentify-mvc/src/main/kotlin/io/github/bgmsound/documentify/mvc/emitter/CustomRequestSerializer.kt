package io.github.bgmsound.documentify.mvc.emitter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.restdocs.operation.OperationRequest
import org.springframework.restdocs.operation.OperationRequestFactory
import org.springframework.restdocs.operation.OperationResponse
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor

class CustomRequestSerializer(
    private val objectMapper: ObjectMapper
): OperationPreprocessor {
    override fun preprocess(request: OperationRequest): OperationRequest {
        if (request.content.isEmpty()) {
            return request
        }
        val content = objectMapper.readValue(request.content, Any::class.java)
        return OperationRequestFactory()
            .createFrom(request, objectMapper.writeValueAsBytes(content))
    }

    override fun preprocess(response: OperationResponse): OperationResponse {
        return response
    }
}