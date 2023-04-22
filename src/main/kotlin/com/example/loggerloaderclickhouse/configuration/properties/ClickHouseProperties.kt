package com.example.loggerloaderclickhouse.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("clickhouse")
class ClickHouseProperties {
    lateinit var clickhouseJdbcUrl: String
    lateinit var searchTable: String

    var auth = ClickhouseAuth()

    var batchSize = 100

    var socketTimeout = 30000
    var connectTimeout = 10000
    var writeTimeout = 10000
    var backoffTimeout = 3000L

    class ClickhouseAuth {
        lateinit var username: String
        lateinit var password: String
    }
}
