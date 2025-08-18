package com.oam.weatherapp.domain.usecase

import com.oam.weatherapp.data.local.entity.WeatherEntity
import com.oam.weatherapp.domain.model.WeatherInfo
import com.oam.weatherapp.domain.repository.WeatherRepository
import com.oam.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow


class GetWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(city: String): Flow<Resource<WeatherEntity?>> {
        return repository.getWeatherData(city)
    }
}
