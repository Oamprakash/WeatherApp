package com.oam.weatherapp.presentation.uistate

import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.domain.model.WeatherInfo

sealed class ForecastUiState {
    object Loading : ForecastUiState()
    data class Success(val forecastDay: ForecastDay) : ForecastUiState()
    data class Error(val message: String) : ForecastUiState()
}