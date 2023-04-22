package com.example.loggerloaderclickhouse.kafka

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.TopicPartition
import org.springframework.kafka.listener.ConsumerSeekAware.ConsumerSeekCallback
import org.springframework.kafka.support.Acknowledgment

enum class OffsetAction {
    DO_NOTHING,
    COMMIT_OFFSET,
    SEEK_TO_LAST_COMMITED_OFFSET
}

fun consumeAtLeastOnce(
    partitions: Set<TopicPartition>,
    consumer: Consumer<*, *>,
    acknowledgment: Acknowledgment,
    consumerSeekCallback: ConsumerSeekCallback,
    consumeAction: () -> OffsetAction
) {
    when (consumeAction()) {
        OffsetAction.DO_NOTHING -> {}
        OffsetAction.COMMIT_OFFSET -> acknowledgment.acknowledge()
        OffsetAction.SEEK_TO_LAST_COMMITED_OFFSET -> seekToLastCommitedOffset(partitions, consumer, consumerSeekCallback)
    }
}

private fun seekToLastCommitedOffset(
    partitions: Set<TopicPartition>,
    consumer: Consumer<*, *>,
    callback: ConsumerSeekCallback
) {
    for ((partition, offset) in consumer.committed(partitions)) {
        callback.seek(
            partition.topic(),
            partition.partition(),
            offset.offset()
        )
    }
}
