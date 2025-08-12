package com.oam.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.domain.model.WeatherInfo

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false)
    val cityName: String,
    val temperature: Double,
    val description: String,
    val icon: String,
    val lastUpdated: Long
)

fun WeatherEntity.toDomain(): WeatherInfo {
    return WeatherInfo(
        cityName = cityName,
        temperature = temperature,
        description = description,
        iconUrl = icon
    )
}

fun WeatherInfo.toEntity(city: String): WeatherEntity {
    return WeatherEntity(
        temperature = temperature,
        description = description,
        icon = iconUrl,
        cityName = city,
        lastUpdated = TODO()
    )
}