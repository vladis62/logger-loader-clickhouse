package com.example.loggerloaderclickhouse.service.impl

import com.example.loggerloaderclickhouse.configuration.properties.ClickHouseProperties
import com.example.loggerloaderclickhouse.service.ClickHouseService
import org.springframework.retry.support.RetryTemplateBuilder
import org.springframework.stereotype.Service
import com.example.loggerloaderclickhouse.service.impl.util.ClickHouseSearchBuffer
import ru.yandex.clickhouse.ClickHouseConnection
import javax.sql.DataSource

private const val BACKOFF_MULTIPLIER = 1.5
private const val MAX_BACKOFF_MS = 200000L // less than max.poll.interval.ms

@Service
class ClickHouseServiceImpl(
    clickHouseProperties: ClickHouseProperties,
    private val dataSource: DataSource
) : ClickHouseService {

    private val retryTemplate = RetryTemplateBuilder()
        .withinMillis(MAX_BACKOFF_MS)
        .exponentialBackoff(clickHouseProperties.backoffTimeout, BACKOFF_MULTIPLIER, Long.MAX_VALUE)
        .build()

    override fun save(searchBuffer: ClickHouseSearchBuffer) {
        retryTemplate.execute<Unit, Exception> {
            dataSource.connection.use {
                searchBuffer.sendRecords(it.unwrap(ClickHouseConnection::class.java))
            }
        }
    }
}
