package com.oam.weatherapp.domain.model

data class WeatherInfo(
    val cityName: String,
    val temperature: Double,
    val description: String,
    val iconUrl: String
)
