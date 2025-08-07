package com.oam.weatherapp.data.model

data class WeatherDto(
    val name: String,
    val main: MainDto,
    val weather: List<WeatherDescriptionDto>
)

data class MainDto(
    val temp: Double
)

data class WeatherDescriptionDto(
    val description: String,
    val icon: String
)
