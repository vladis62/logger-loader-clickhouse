package com.example.loggerloaderclickhouse.model

import java.time.Instant

data class ClickHouseSearchColumns(
    val auditTimeHour: Instant,
    val applicationName: String,
    val entityId: String,
    val auditTime: Instant,
    val auditPoint: String,
    val isError: Boolean,
)

