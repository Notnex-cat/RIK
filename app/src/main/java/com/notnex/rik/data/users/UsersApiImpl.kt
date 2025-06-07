package com.notnex.rik.data.users

import com.notnex.rik.data.users.model.UsersResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class UsersApiImpl : UsersApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    override suspend fun getUsers(): UsersResponse {
        return client.get("http://test.rikmasters.ru/api/users/").body()
    }
} 