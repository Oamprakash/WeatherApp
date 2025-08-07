package com.oam.weatherapp.presentation.uistate

import com.oam.weatherapp.domain.model.WeatherInfo

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weatherInfo: WeatherInfo) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
