package com.example.loggerloaderclickhouse.service

import com.example.loggerloaderclickhouse.service.impl.util.ClickHouseSearchBuffer

interface ClickHouseService {

    fun save(searchBuffer: ClickHouseSearchBuffer)
}
