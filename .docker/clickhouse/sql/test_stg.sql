CREATE DATABASE IF NOT EXISTS test_stg;

CREATE TABLE IF NOT EXISTS test_stg.logger_search
(
    key_hash Int64,
    entityId String,
    auditPoint LowCardinality(String),
    auditTime UInt64,
    isError UInt8,
    applicationName LowCardinality(String),
    auditTimeHour DateTime
)
ENGINE = Distributed('local_cluster', 'test_s_stg', 'logger_search', key_hash);
