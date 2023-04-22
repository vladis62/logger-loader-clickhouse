package com.example.loggerloaderclickhouse.service.impl

import com.example.loggerloaderclickhouse.model.AuditKafkaMessage
import com.example.loggerloaderclickhouse.model.ClickHouseSearchColumns
import java.time.temporal.ChronoUnit.HOURS
import java.time.temporal.ChronoUnit.SECONDS

internal object AuditMapper {

    fun toSearchColumns(message: AuditKafkaMessage) = ClickHouseSearchColumns(
        auditTimeHour = message.auditTime.truncatedTo(HOURS),
        applicationName = message.applicationName.orEmpty(),
        entityId = message.entityId,
        auditTime = message.auditTime.truncatedTo(SECONDS),
        auditPoint = message.auditPoint,
        isError = message.isError
    )
}
