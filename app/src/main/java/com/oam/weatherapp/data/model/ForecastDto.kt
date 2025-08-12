package com.oam.weatherapp.data.model

// data/remote/dto/ForecastDto.kt
data class ForecastDto(
    val list: List<ForecastItemDto>
)

data class ForecastItemDto(
    val dt: Long,               // unix timestamp
    val main: MainDto,
    val weather: List<WeatherDescriptionDto>
)

/*data class MainDto(
    val temp: Double
)

data class WeatherDescriptionDto(
    val description: String,
    val icon: String
)*/
