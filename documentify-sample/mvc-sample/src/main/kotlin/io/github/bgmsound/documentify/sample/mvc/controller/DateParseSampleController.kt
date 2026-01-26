package io.github.bgmsound.documentify.sample.mvc.controller

import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/date-parse-sample")
class DateParseSampleController {
    private val logger = LoggerFactory.getLogger(DateParseSampleController::class.java)

    @GetMapping
    fun dateParseSample(
        @RequestParam("time", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") time: LocalDateTime?
    ) {
        logger.info("Parsed time: {}", time)
    }
}