package com.oam.weatherapp.data.mapper

import com.oam.weatherapp.data.model.ForecastDto
import com.oam.weatherapp.data.model.ForecastItemDto
import com.oam.weatherapp.domain.model.ForecastDay

// data/mapper/ForecastMappers.kt
fun ForecastItemDto.toDomain(): ForecastDay {
    return ForecastDay(
        timestamp = dt,
        temperature = main.temp,
        description = weather.firstOrNull()?.description ?: "N/A",
        icon = weather.firstOrNull()?.icon
    )
}

fun ForecastDto.toDomainList(): List<ForecastDay> {
    return list.map { it.toDomain() }
}
