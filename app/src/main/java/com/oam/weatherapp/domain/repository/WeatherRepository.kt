package com.oam.weatherapp.domain.repository

import com.oam.weatherapp.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherData(city: String): Flow<WeatherInfo>
}
