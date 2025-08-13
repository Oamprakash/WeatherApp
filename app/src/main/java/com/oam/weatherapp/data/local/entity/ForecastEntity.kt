package com.oam.weatherapp.data.local.entity// data/local/entity/ForecastEntity.kt

import androidx.room.Entity
import com.oam.weatherapp.domain.model.ForecastDay

@Entity(tableName = "forecast_table",  primaryKeys = ["city", "timestamp"])
data class ForecastEntity(
    val timestamp: Long,
    val city: String,
    val temperature: Double,
    val description: String,
    val icon: String?,
    val lastUpdated: Long
)

fun ForecastEntity.toDomain(): ForecastDay {
    return ForecastDay(
        timestamp = timestamp,
        temperature = temperature,
        description = description,
        icon = icon,
        lastUpdated = lastUpdated,
    )
}

fun ForecastDay.toEntity(city: String, lastUpdated: Long): ForecastEntity {
    return ForecastEntity(
        timestamp = timestamp,
        city = city,
        temperature = temperature,
        description = description,
        icon = icon,
        lastUpdated = lastUpdated
    )
}
