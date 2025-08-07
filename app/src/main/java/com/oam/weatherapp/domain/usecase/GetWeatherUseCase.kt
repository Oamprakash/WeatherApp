package com.oam.weatherapp.domain.usecase

import com.oam.weatherapp.domain.model.WeatherInfo
import com.oam.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow


class GetWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(city: String): Flow<WeatherInfo> {
        return repository.getWeatherData(city)
    }
}
