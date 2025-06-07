package com.notnex.rik.data.users.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val users: List<User>
)

@Serializable
data class User(
    val id: Int,
    val sex: String, // "M" или "W"
    val username: String,
    val isOnline: Boolean,
    val age: Int,
    val files: List<UserFile>
)

@Serializable
data class UserFile(
    val id: Int,
    val url: String,
    val type: String // "avatar"
) 