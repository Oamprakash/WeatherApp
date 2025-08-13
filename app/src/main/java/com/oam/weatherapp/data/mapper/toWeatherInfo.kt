package com.oam.weatherapp.data.mapper

import com.oam.weatherapp.data.model.WeatherDto
import com.oam.weatherapp.domain.model.WeatherInfo

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
        cityName = name,
        temperature = main.temp,
        description = weather.firstOrNull()?.description ?: "No description",
        iconUrl = "https://openweathermap.org/img/wn/${weather.firstOrNull()?.icon}@2x.png",
        lastUpdated = System.currentTimeMillis()
    )
}
