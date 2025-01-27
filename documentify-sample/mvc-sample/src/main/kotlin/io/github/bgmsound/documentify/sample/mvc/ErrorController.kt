package io.github.bgmsound.documentify.sample.mvc

import io.github.bgmsound.documentify.sample.mvc.dto.response.SampleResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/error")
class ErrorController {
    @GetMapping
    fun error(
        @RequestParam("throw") throwError: Boolean
    ): Any {
        if (throwError) {
            return ResponseEntity.status(400).body(ErrorResponse(400, "error"))
        }
        return SampleResponse(200, "ok")
    }

    data class ErrorResponse(
        val code: Int,
        val message: String
    )
}