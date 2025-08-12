package com.oam.weatherapp.data.local.entity// data/local/entity/ForecastEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.oam.weatherapp.domain.model.ForecastDay

@Entity(tableName = "forecast_table")
data class ForecastEntity(
    @PrimaryKey val timestamp: Long,
    val city: String,
    val temperature: Double,
    val description: String,
    val icon: String?
)

fun ForecastEntity.toDomain(): ForecastDay {
    return ForecastDay(
        timestamp = timestamp,
        temperature = temperature,
        description = description,
        icon = icon
    )
}

fun ForecastDay.toEntity(city: String): ForecastEntity {
    return ForecastEntity(
        timestamp = timestamp,
        city = city,
        temperature = temperature,
        description = description,
        icon = icon
    )
}
