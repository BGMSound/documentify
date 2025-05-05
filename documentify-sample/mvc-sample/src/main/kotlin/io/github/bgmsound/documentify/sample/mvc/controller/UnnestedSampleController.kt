package io.github.bgmsound.documentify.sample.mvc.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/unnested-sample")
class UnnestedSampleController {
    @GetMapping
    fun unnestedSample(): String {
        return "unnested sample"
    }

    @GetMapping("/list")
    fun unnestedSampleList(): List<String> {
        return listOf("unnested sample 1", "unnested sample 2")
    }
}