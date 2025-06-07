package com.notnex.rik.data.users

import com.notnex.rik.data.users.model.UsersResponse

interface UsersApi {
    suspend fun getUsers(): UsersResponse
} 