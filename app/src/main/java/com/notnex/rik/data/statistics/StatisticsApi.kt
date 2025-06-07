package com.notnex.rik.data.statistics

import com.notnex.rik.data.statistics.model.StatisticsResponse

interface StatisticsApi {
    suspend fun getStatistics(): StatisticsResponse
} 