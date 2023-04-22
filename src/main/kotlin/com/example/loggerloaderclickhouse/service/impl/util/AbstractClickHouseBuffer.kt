package com.example.loggerloaderclickhouse.service.impl.util

import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream
import org.apache.commons.compress.utils.CountingOutputStream
import org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream
import ru.yandex.clickhouse.ClickHouseConnection
import ru.yandex.clickhouse.domain.ClickHouseCompression
import ru.yandex.clickhouse.domain.ClickHouseFormat
import ru.yandex.clickhouse.settings.ClickHouseProperties
import ru.yandex.clickhouse.util.ClickHouseRowBinaryStream
import java.util.TimeZone

private const val INITIAL_BUFFER_SIZE = 1 * 1024 * 1024
private const val ZSTD_LEVEL = 1

abstract class AbstractClickHouseBuffer<T> {
    private val dummyProperties = ClickHouseProperties()
    // timeZone используется только в методе writeDate, который мы не вызываем
    private val dummyTimeZone: TimeZone? = null
    private val buffer = UnsynchronizedByteArrayOutputStream(INITIAL_BUFFER_SIZE)
    private lateinit var writeStream: CountingOutputStream
    private lateinit var clickHouseRowBinaryStream: ClickHouseRowBinaryStream
    private var recordsWritten = 0

    init {
        startNewBatch()
    }

    fun startNewBatch() {
        recordsWritten = 0
        buffer.reset()
        writeStream = CountingOutputStream(ZstdCompressorOutputStream(buffer, ZSTD_LEVEL))
        clickHouseRowBinaryStream = ClickHouseRowBinaryStream(writeStream, dummyTimeZone, dummyProperties)
    }

    fun finishBatch() {
        writeStream.close()
    }

    fun append(record: T) {
        clickHouseRowBinaryStream.append(record)
        recordsWritten += 1
    }

    protected abstract fun ClickHouseRowBinaryStream.append(record: T)

    fun sendRecords(connection: ClickHouseConnection) {
        connection.createStatement()
            .use {
                it.write()
                    .dataCompression(ClickHouseCompression.zstd)
                    .data(buffer.toInputStream())
                    .format(ClickHouseFormat.RowBinary)
                    .sql(insertStatement)
                    .send()
            }
    }

    protected abstract val insertStatement: String

    val size: Int
        get() = recordsWritten

    val bytesWritten: Long
        get() = writeStream.bytesWritten
}
