package com.example.loggerloaderclickhouse.kafka

import com.example.loggerloaderclickhouse.model.AuditKafkaMessage
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.listener.ConsumerSeekAware.ConsumerSeekCallback
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import com.example.loggerloaderclickhouse.service.AuditSaverService
import java.time.Instant

@Component
class AuditKafkaListener(
    private val auditSaverService: AuditSaverService
) : ConsumerSeekAware {

    private val seekCallback = ThreadLocal<ConsumerSeekCallback>()

    @KafkaListener(
        topics = ["\${kafka.topic-name}"],
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun onMessage(
        records: ConsumerRecords<*, AuditKafkaMessage?>,
        acknowledgment: Acknowledgment,
        consumer: Consumer<*, *>
    ) {
        consumeAtLeastOnce(records.partitions(), consumer, acknowledgment, seekCallback.get()) {
            auditSaverService.save(records.mapNotNull { it.value() })
        }
    }

    override fun registerSeekCallback(callback: ConsumerSeekCallback) {
        seekCallback.set(callback)
    }
}
