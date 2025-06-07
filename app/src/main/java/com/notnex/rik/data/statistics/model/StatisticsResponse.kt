package com.notnex.rik.data.statistics.model

import kotlinx.serialization.Serializable

@Serializable
data class StatisticsResponse(
    val statistics: List<Statistic>
)

@Serializable
data class Statistic(
    val user_id: Int,
    val type: String, // "view", "subscription", "unsubscription"
    val dates: List<Long>
)

@Serializable
data class TopVisitor(
    val avatarUrl: String?,
    val name: String,
    val age: Int,
    val emoji: String
)

@Serializable
data class GenderStats(
    val malePercent: Int,
    val femalePercent: Int
)

@Serializable
data class AgeStat(
    val range: String,
    val malePercent: Int,
    val femalePercent: Int
) 