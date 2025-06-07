package com.notnex.rik.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notnex.rik.data.statistics.StatisticsApiImpl
import com.notnex.rik.data.statistics.StatisticsRepository
import com.notnex.rik.data.statistics.model.StatisticsResponse
import com.notnex.rik.data.users.UsersApiImpl
import com.notnex.rik.data.users.UsersRepository
import com.notnex.rik.data.users.model.UsersResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

sealed class StatisticsState {
    object Loading : StatisticsState()
    data class Success(val users: UsersResponse, val statistics: StatisticsResponse) : StatisticsState()
    data class Error(val message: String) : StatisticsState()
}

enum class ChartMode { DAY, WEEK, MONTH }
enum class GenderAgeMode { DAY, WEEK, MONTH, ALL }

class StatisticsViewModel : ViewModel() {
    private val statisticsRepository = StatisticsRepository(StatisticsApiImpl())
    private val usersRepository = UsersRepository(UsersApiImpl())
    private val _state = MutableStateFlow<StatisticsState>(StatisticsState.Loading)
    val state: StateFlow<StatisticsState> = _state

    private val _chartMode = MutableStateFlow(ChartMode.DAY)
    val chartMode: StateFlow<ChartMode> = _chartMode

    private val _genderAgeMode = MutableStateFlow(GenderAgeMode.DAY)
    val genderAgeMode: StateFlow<GenderAgeMode> = _genderAgeMode

    fun setChartMode(mode: ChartMode) {
        _chartMode.value = mode
    }

    fun setGenderAgeMode(mode: GenderAgeMode) {
        _genderAgeMode.value = mode
    }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val usersDeferred = async { usersRepository.getUsers() }
                val statisticsDeferred = async { statisticsRepository.getStatistics() }
                val users = usersDeferred.await()
                val statistics = statisticsDeferred.await()
                _state.value = StatisticsState.Success(users, statistics)
            } catch (e: Exception) {
                _state.value = StatisticsState.Error(e.message ?: "Unknown error")
            }
        }
    }
} 