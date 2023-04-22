package com.example.loggerloaderclickhouse.controller

import com.example.loggerloaderclickhouse.model.AuditKafkaMessage
import com.example.loggerloaderclickhouse.service.AuditSaverService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/test")
class Test(
    private val auditSaverService: AuditSaverService
) {

    @GetMapping
    fun test() {
        val newRecords = mutableListOf<AuditKafkaMessage>()
        for (i in 0..1000) {
//            Thread.sleep(10)
            newRecords.add(AuditKafkaMessage("123", Instant.now(), "entity", i % 100 == 0, "applicationName"))
        }
        auditSaverService.save(newRecords)
    }
}