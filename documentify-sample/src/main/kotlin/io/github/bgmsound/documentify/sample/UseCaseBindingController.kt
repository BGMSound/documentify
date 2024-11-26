package io.github.bgmsound.documentify.sample

import io.github.bgmsound.documentify.sample.dto.response.SampleResponse
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/use-case")
class UseCaseBindingController(
    private val useCase: UseCase
) {
    @GetMapping
    fun useCaseBinding(): SampleResponse {
        return useCase.doSomethingAndReturn()
    }
}

@Service
class UseCase {
    fun doSomethingAndReturn(): SampleResponse {
        return SampleResponse(1, "usecase binding")
    }
}