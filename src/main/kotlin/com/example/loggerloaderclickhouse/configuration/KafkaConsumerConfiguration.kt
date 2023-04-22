package com.example.loggerloaderclickhouse.configuration

import com.example.loggerloaderclickhouse.configuration.properties.KafkaConsumerProperties
import com.example.loggerloaderclickhouse.kafka.AuditKafkaDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import com.example.loggerloaderclickhouse.model.AuditKafkaMessage

@Configuration
class KafkaConsumerConfiguration {

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, AuditKafkaMessage> {
        val factory = DefaultKafkaConsumerFactory(
            KafkaConsumerProperties().toConsumerConfigs(),
            StringDeserializer(),
            AuditKafkaDeserializer()
        )

        return ConcurrentKafkaListenerContainerFactory<String, AuditKafkaMessage>().apply {
            consumerFactory = factory
            isBatchListener = true
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }
    }
}
