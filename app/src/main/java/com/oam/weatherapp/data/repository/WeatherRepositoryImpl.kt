package com.oam.weatherapp.data.repository

import com.oam.weatherapp.data.remote.WeatherApi
import com.oam.weatherapp.domain.model.WeatherInfo
import com.oam.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getWeatherData(city: String): Flow<WeatherInfo> = flow {
        val response = api.getWeatherByCity(city, "YOUR_API_KEY")
        emit(
            WeatherInfo(
                temperature = response.main.temp,
                description = response.weather.firstOrNull()?.description ?: "",
                cityName = response.name,
                iconUrl = TODO()
            )
        )
    }
}
