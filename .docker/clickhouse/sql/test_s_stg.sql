CREATE DATABASE IF NOT EXISTS test_s_stg;

CREATE TABLE IF NOT EXISTS test_s_stg.logger_search
(
    key_hash Int64,
    entityId String,
    auditPoint LowCardinality(String),
    auditTime UInt64,
    isError UInt8,
    applicationName LowCardinality(String),
    auditTimeHour DateTime
)
ENGINE = ReplicatedReplacingMergeTree('/clickhouse/tables/test_s_stg/{shard}/logger_search', '{replica}')
PARTITION BY auditTimeHour
PRIMARY KEY (auditTime, key_hash)
ORDER BY (auditTime, key_hash);

