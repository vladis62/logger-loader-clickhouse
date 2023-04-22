package com.example.loggerloaderclickhouse.service

import com.example.loggerloaderclickhouse.kafka.OffsetAction
import com.example.loggerloaderclickhouse.model.AuditKafkaMessage

interface AuditSaverService {

    fun save(batch: List<AuditKafkaMessage>): OffsetAction
}
