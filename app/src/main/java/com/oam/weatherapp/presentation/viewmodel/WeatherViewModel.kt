package com.oam.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oam.weatherapp.domain.usecase.GetWeatherUseCase
import com.oam.weatherapp.presentation.uistate.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun loadWeather(city: String) {
        viewModelScope.launch {
            getWeatherUseCase(city)
                .onStart { _uiState.value = WeatherUiState.Loading }
                .catch { e -> _uiState.value = WeatherUiState.Error(e.message ?: "Unknown error")
                println("oam e "+e.message)
                }
                .collectLatest { weather ->
                    _uiState.value = WeatherUiState.Success(weather)
                    println("oam "+weather.toString() + "")
                }
        }
    }
}
