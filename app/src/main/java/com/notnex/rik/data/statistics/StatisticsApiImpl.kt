package com.notnex.rik.data.statistics

import com.notnex.rik.data.statistics.model.StatisticsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class StatisticsApiImpl : StatisticsApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    override suspend fun getStatistics(): StatisticsResponse {
        return client.get("http://test.rikmasters.ru/api/statistics/").body()
    }
} 