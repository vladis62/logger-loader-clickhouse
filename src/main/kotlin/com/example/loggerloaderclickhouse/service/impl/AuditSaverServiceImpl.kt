package com.example.loggerloaderclickhouse.service.impl

import com.example.loggerloaderclickhouse.configuration.properties.ClickHouseProperties
import com.example.loggerloaderclickhouse.kafka.OffsetAction
import com.example.loggerloaderclickhouse.model.AuditKafkaMessage
import com.example.loggerloaderclickhouse.service.AuditSaverService
import org.springframework.stereotype.Service
import com.example.loggerloaderclickhouse.service.ClickHouseService
import com.example.loggerloaderclickhouse.service.impl.util.ClickHouseSearchBuffer

@Service
class AuditSaverServiceImpl(
    private val clickHouseService: ClickHouseService,
    private val clickHouseProperties: ClickHouseProperties
) : AuditSaverService {

    private val searchBuffer = ClickHouseSearchBuffer(clickHouseProperties)

    override fun save(batch: List<AuditKafkaMessage>): OffsetAction {
        fillBuffers(batch)

        return if (searchBuffer.size >= clickHouseProperties.batchSize) {
            searchBuffer.finishBatch()
            saveToClickhouse().also {
                searchBuffer.startNewBatch()
            }
        } else {
            OffsetAction.DO_NOTHING
        }
    }

    private fun fillBuffers(batch: List<AuditKafkaMessage>) {
        batch.forEach {
            searchBuffer.append(AuditMapper.toSearchColumns(it))
        }
    }

    private fun saveToClickhouse(): OffsetAction =
        try {
            clickHouseService.save(searchBuffer)
            OffsetAction.COMMIT_OFFSET
        } catch (ex: Exception) {
            OffsetAction.SEEK_TO_LAST_COMMITED_OFFSET
        }
}
