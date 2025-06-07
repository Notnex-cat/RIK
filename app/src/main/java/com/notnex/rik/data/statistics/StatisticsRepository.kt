package com.notnex.rik.data.statistics

import com.notnex.rik.data.statistics.model.StatisticsResponse

class StatisticsRepository(private val api: StatisticsApi) {
    suspend fun getStatistics(): StatisticsResponse {
        return api.getStatistics()
    }
} 