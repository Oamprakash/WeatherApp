package com.oam.weatherapp.presentation.uistate

import com.oam.weatherapp.data.local.entity.WeatherEntity

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weatherInfo: WeatherEntity?) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
