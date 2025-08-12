package com.oam.weatherapp.domain.repository

import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.domain.model.WeatherInfo
import com.oam.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherData(city: String): Flow<WeatherInfo>
    suspend fun getForecast(city: String): Flow<Resource<List<ForecastDay>>>
    fun getCachedForecastFlow(city: String): Flow<List<ForecastDay>>
}
