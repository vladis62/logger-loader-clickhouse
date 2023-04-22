package com.example.loggerloaderclickhouse.configuration.properties

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.config.SaslConfigs

class KafkaConsumerProperties {
    var bootstrapServers: String = "localhost:9092"
    var groupId: String = "logger"
    var autoOffsetReset: String = "latest"
    var isolationLevelConfig: String = "read_committed"

    var auth: KafkaAuthorizationProperties = KafkaAuthorizationProperties()

    class KafkaAuthorizationProperties {
        var user: String = "kafka"
        var password: String = "kafka"

        var securityProtocol: String = "SASL_PLAINTEXT"
        var saslMechanism: String = "SCRAM-SHA-512"
        var saslJaas: String = "org.apache.kafka.common.security.scram.ScramLoginModule required"

        val saslJaasConsumerConfig: String
            get() = "$saslJaas username='$user' password='$password';"
    }

    fun toConsumerConfigs(): Map<String, Any> = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false, // manual acknowledge
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to autoOffsetReset,
        ConsumerConfig.ISOLATION_LEVEL_CONFIG to isolationLevelConfig,
        ConsumerConfig.GROUP_ID_CONFIG to groupId,

        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to auth.securityProtocol,
        SaslConfigs.SASL_MECHANISM to auth.saslMechanism,
        SaslConfigs.SASL_JAAS_CONFIG to auth.saslJaasConsumerConfig
    )
}
