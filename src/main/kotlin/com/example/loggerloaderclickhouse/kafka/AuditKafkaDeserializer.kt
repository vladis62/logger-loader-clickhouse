package com.example.loggerloaderclickhouse.kafka

import com.example.loggerloaderclickhouse.model.AuditKafkaMessage
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import org.apache.kafka.common.serialization.Deserializer
import java.time.Instant

class AuditKafkaDeserializer : Deserializer<AuditKafkaMessage?> {
    private val cborFactory = CBORFactory()

    @SuppressWarnings("ComplexMethod")
    override fun deserialize(topic: String, data: ByteArray): AuditKafkaMessage? {
        try {
            val builder = AuditKafkaMessageBuilder()
            val parser = cborFactory.createParser(data)
            while (parser.nextValue() != null) {
                when (parser.currentName) {
                    "auditPoint" -> builder.auditPoint = parser.valueAsString
                    "auditTime" -> builder.auditTime = Instant.ofEpochMilli(parser.valueAsLong)
                    "entityId" -> builder.entityId = parser.valueAsString
                    "error" -> builder.isError = parser.valueAsBoolean
                    "applicationName" -> builder.applicationName = parser.valueAsString
                }
            }
            return builder.build()
        } catch (ex: Exception) {
            return null
        }
    }

    private class AuditKafkaMessageBuilder {
        var auditPoint: String? = null
        var auditTime: Instant? = null
        var entityId: String? = null
        var isError: Boolean? = null
        var applicationName: String? = null

        fun build(): AuditKafkaMessage = AuditKafkaMessage(
            auditPoint = auditPoint ?: throw IllegalStateException("No auditPoint"),
            auditTime = auditTime ?: throw IllegalStateException("No auditTime"),
            entityId = entityId ?: throw IllegalStateException("No entityId"),
            isError = isError ?: false,
            applicationName = applicationName,
        )
    }
}
