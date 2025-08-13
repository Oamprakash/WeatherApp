package com.oam.weatherapp.domain.model

// domain/model/ForecastDay.kt
data class ForecastDay(
    val timestamp: Long,     // unix seconds
    val temperature: Double,
    val description: String,
    val icon: String?,
    val lastUpdated: Long
)
