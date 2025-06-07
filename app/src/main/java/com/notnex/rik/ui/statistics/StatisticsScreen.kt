package com.notnex.rik.ui.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.notnex.rik.data.statistics.model.VisitorsChartPoint
import com.notnex.rik.ui.statistics.components.AgeStatUi
import com.notnex.rik.ui.statistics.components.ChartModeTabSelector
import com.notnex.rik.ui.statistics.components.GenderAgeChart
import com.notnex.rik.ui.statistics.components.GenderAgeTabSelector
import com.notnex.rik.ui.statistics.components.ObserversCard
import com.notnex.rik.ui.statistics.components.TopVisitorsList
import com.notnex.rik.ui.statistics.components.VisitorsCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(modifier: Modifier = Modifier) {
    val viewModel: StatisticsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val chartMode by viewModel.chartMode.collectAsState()
    val genderAgeMode by viewModel.genderAgeMode.collectAsState()

    when (state) {
        is StatisticsState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is StatisticsState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = (state as StatisticsState.Error).message)
            }
        }

        is StatisticsState.Success -> {
            val users = (state as StatisticsState.Success).users.users
            val statistics = (state as StatisticsState.Success).statistics.statistics
            val totalViews = statistics.filter { it.type == "view" }.sumOf { it.dates.size }
            val dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy")

            val chartModeEnum = listOf(ChartMode.DAY, ChartMode.WEEK, ChartMode.MONTH)
            val selectedTabIndex = chartModeEnum.indexOf(chartMode)

            val chartPoints = when (chartMode) {
                ChartMode.DAY -> statistics
                    .filter { it.type == "view" }
                    .flatMap { it.dates }
                    .groupingBy {
                        val dateStr = it.toString().padStart(8, '0')
                        LocalDate.parse(dateStr, dateFormatter)
                    }
                    .eachCount()
                    .toList()
                    .sortedBy { it.first }
                    .map { (date, count) -> VisitorsChartPoint(date.format(DateTimeFormatter.ofPattern("dd.MM")), count) }

                ChartMode.WEEK -> statistics
                    .filter { it.type == "view" }
                    .flatMap { it.dates }
                    .groupingBy {
                        val dateStr = it.toString().padStart(8, '0')
                        val date = LocalDate.parse(dateStr, dateFormatter)
                        date.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR) to date.year
                    }
                    .eachCount()
                    .toList()
                    .sortedWith(compareBy({ it.first.second }, { it.first.first }))
                    .map { (weekYear, count) ->
                        val (week, year) = weekYear
                        VisitorsChartPoint("${week}н\n$year", count)
                    }

                ChartMode.MONTH -> statistics
                    .filter { it.type == "view" }
                    .flatMap { it.dates }
                    .groupingBy {
                        val dateStr = it.toString().padStart(8, '0')
                        val date = LocalDate.parse(dateStr, dateFormatter)
                        date.monthValue to date.year
                    }
                    .eachCount()
                    .toList()
                    .sortedWith(compareBy({ it.first.second }, { it.first.first }))
                    .map { (monthYear, count) ->
                        val (month, year) = monthYear
                        VisitorsChartPoint("%02d.%d".format(month, year), count)
                    }

                else -> emptyList()
            }

            val viewsByUser = statistics.filter { it.type == "view" }
                .groupBy { it.user_id }
                .mapValues { it.value.sumOf { entry -> entry.dates.size } }

            val topUsers = users.mapNotNull { user ->
                viewsByUser[user.id]?.let { user to it }
            }.sortedByDescending { it.second }

            val visitorIds = statistics.filter { it.type == "view" }.map { it.user_id }.toSet()
            val visitorUsers = users.filter { it.id in visitorIds }

            val genderAgeModeEnum = listOf(GenderAgeMode.DAY, GenderAgeMode.WEEK, GenderAgeMode.MONTH, GenderAgeMode.ALL)
            val selectedGenderAgeTab = genderAgeModeEnum.indexOf(genderAgeMode)

            val now = LocalDate.now()
            val filteredVisitorUsers = when (genderAgeMode) {
                GenderAgeMode.DAY -> visitorUsers.filter { user ->
                    statistics.any { it.user_id == user.id && it.type == "view" && it.dates.any { d ->
                        LocalDate.parse(d.toString().padStart(8, '0'), dateFormatter) == now
                    } }
                }
                GenderAgeMode.WEEK -> visitorUsers.filter { user ->
                    statistics.any { it.user_id == user.id && it.type == "view" && it.dates.any { d ->
                        val date = LocalDate.parse(d.toString().padStart(8, '0'), dateFormatter)
                        val week = date.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                        val year = date.year
                        val nowWeek = now.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                        val nowYear = now.year
                        week == nowWeek && year == nowYear
                    } }
                }
                GenderAgeMode.MONTH -> visitorUsers.filter { user ->
                    statistics.any { it.user_id == user.id && it.type == "view" && it.dates.any { d ->
                        val date = LocalDate.parse(d.toString().padStart(8, '0'), dateFormatter)
                        date.monthValue == now.monthValue && date.year == now.year
                    } }
                }
                GenderAgeMode.ALL -> visitorUsers
            }

            val maleCount = filteredVisitorUsers.count { it.sex == "M" }
            val femaleCount = filteredVisitorUsers.count { it.sex == "W" }
            val total = (maleCount + femaleCount).takeIf { it > 0 } ?: 1
            val malePercent = (maleCount * 100) / total
            val femalePercent = (femaleCount * 100) / total

            val ageGroups = listOf(
                "0-18" to (0..17),
                "18-21" to (18..21),
                "22-25" to (22..25),
                "26-30" to (26..30),
                "31-35" to (31..35),
                "36-40" to (36..40),
                "40-50" to (40..50),
                ">50" to (51..120)
            )

            val ageStats = ageGroups.map { (label, range) ->
                val males = filteredVisitorUsers.count { it.sex == "M" && it.age in range }
                val females = filteredVisitorUsers.count { it.sex == "W" && it.age in range }
                val totalGroup = (males + females).takeIf { it > 0 } ?: 1

                AgeStatUi(
                    range = label,
                    malePercent = (males * 100) / totalGroup,
                    femalePercent = (females * 100) / totalGroup
                )
            }

            val newObservers = statistics.filter { it.type == "subscription" }.sumOf { it.dates.size }
            val leftObservers = statistics.filter { it.type == "unsubscription" }.sumOf { it.dates.size }
            val observersGrowth = newObservers >= leftObservers

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
            ) {
                //график
                item {
                    ChartModeTabSelector(
                        selected = selectedTabIndex,
                        onTabSelected = { viewModel.setChartMode(chartModeEnum[it]) }
                    )
                }



                item { VisitorsCard(visitorsCount = totalViews, visitorsGrowth = true, chartPoints = chartPoints) }
                item { TopVisitorsList(topUsers = topUsers) }

                //пол и возраст
                item {
                    GenderAgeTabSelector(
                        selected = selectedGenderAgeTab,
                        onTabSelected = { viewModel.setGenderAgeMode(genderAgeModeEnum[it]) }
                    )
                }

                item {
                    GenderAgeChart(
                        malePercent = malePercent,
                        femalePercent = femalePercent,
                        ageStats = ageStats
                    )
                }

                item {
                    ObserversCard(
                        newObservers = newObservers,
                        observersGrowth = observersGrowth,
                        leftObservers = leftObservers
                    )
                }
            }
        }
    }
}

