package com.oam.weatherapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.domain.usecase.GetForecastUseCase
import com.oam.weatherapp.domain.usecase.GetWeatherUseCase
import com.oam.weatherapp.presentation.uistate.WeatherUiState
import com.oam.weatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

//    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
//    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _forecastState = MutableStateFlow<Resource<List<ForecastDay>>>(Resource.Loading())
    val forecastState: StateFlow<Resource<List<ForecastDay>>> = _forecastState.asStateFlow()

    init {
        loadWeather("London") // Default on launch
    }


    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun loadWeather(city: String) {
//        val cachedData = forecastDao.getWeather(city)
//        Log.d("WeatherRepo", "Loaded from DB: $cachedData")
        viewModelScope.launch {
            getWeatherUseCase(city)
                .onStart { _uiState.value = WeatherUiState.Loading }
                .catch { e -> //_uiState.value = WeatherUiState.Error(e.message ?: "Unknown error")
                println("oam e "+e.message)
                }
                .collectLatest { weather ->
                    _uiState.value = WeatherUiState.Success(weather)
                    println("oam "+weather.toString() + "")
                }
        }

        // fetch forecast in parallel
        viewModelScope.launch {
            getForecastUseCase(city)
                .onStart { _forecastState.value = Resource.Loading() }
                .catch { e -> _forecastState.value = Resource.Error(e.message ?: "Unknown error")
                    println("oam e "+e.message) }
                .collectLatest { resource ->
                    _forecastState.value = resource
                }
        }
    }
}
