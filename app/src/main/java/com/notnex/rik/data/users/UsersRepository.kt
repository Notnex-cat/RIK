package com.notnex.rik.data.users

import com.notnex.rik.data.users.model.UsersResponse

class UsersRepository(private val api: UsersApi) {
    suspend fun getUsers(): UsersResponse {
        return api.getUsers()
    }
} 