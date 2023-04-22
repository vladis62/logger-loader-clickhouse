package com.example.loggerloaderclickhouse.service.impl.util

import com.example.loggerloaderclickhouse.configuration.properties.ClickHouseProperties
import com.example.loggerloaderclickhouse.model.ClickHouseSearchColumns
import ru.yandex.clickhouse.util.ClickHouseRowBinaryStream

class ClickHouseSearchBuffer(
    private val clickHouseProperties: ClickHouseProperties
) : AbstractClickHouseBuffer<ClickHouseSearchColumns>() {

    override fun ClickHouseRowBinaryStream.append(record: ClickHouseSearchColumns) {
        writeString(record.entityId)
        writeString(record.auditPoint)
        writeUInt32(record.auditTime.epochSecond)
        writeUInt8(record.isError)
        writeString(record.applicationName)
        writeUInt32(record.auditTimeHour.epochSecond)
    }

    override val insertStatement: String
        get() = """
            INSERT INTO ${clickHouseProperties.searchTable} (
            entityId,
            auditPoint,
            auditTime,
            isError,
            applicationName,
            auditTimeHour,
            )
        """.trimIndent()
}
