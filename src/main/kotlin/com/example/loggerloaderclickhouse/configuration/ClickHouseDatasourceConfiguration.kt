package com.example.loggerloaderclickhouse.configuration

import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.example.loggerloaderclickhouse.configuration.properties.ClickHouseProperties
import javax.sql.DataSource

@Configuration
class ClickHouseDatasourceConfiguration {

    @Bean
    fun dataSource(
        clickHouseProperties: ClickHouseProperties
    ): DataSource {
        with(clickHouseProperties) {
            val properties = ru.yandex.clickhouse.settings.ClickHouseProperties().also {
                it.user = auth.username
                it.password = auth.password
                it.socketTimeout = socketTimeout
                it.connectionTimeout = connectTimeout
                it.dataTransferTimeout = writeTimeout
            }

            return HikariDataSource().apply {
                jdbcUrl = clickhouseJdbcUrl
                dataSourceProperties = properties.asProperties()
            }
        }
    }
}
