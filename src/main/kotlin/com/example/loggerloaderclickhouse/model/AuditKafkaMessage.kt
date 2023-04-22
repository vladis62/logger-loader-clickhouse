package com.example.loggerloaderclickhouse.model

import java.time.Instant

data class AuditKafkaMessage(
    val auditPoint: String,
    val auditTime: Instant,
    val entityId: String,
    val isError: Boolean,
    val applicationName: String?,
)
