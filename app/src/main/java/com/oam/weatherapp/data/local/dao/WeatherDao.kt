package com.oam.weatherapp.data.local.dao// data/local/dao/ForecastDao.kt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oam.weatherapp.data.local.entity.ForecastEntity
import com.oam.weatherapp.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    // Store or update current weather
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherEntity: WeatherEntity)

    // Get stored current weather for a specific city
    @Query("SELECT * FROM weather WHERE cityName = :city LIMIT 1")
    fun getWeatherForCity(city: String): Flow<WeatherEntity?>

    @Query("SELECT * FROM forecast_table WHERE city = :city ORDER BY timestamp ASC")
    fun getForecastForCity(city: String): Flow<List<ForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(items: List<ForecastEntity>)

    @Query("DELETE FROM forecast_table WHERE city = :city")
    suspend fun clearForecastForCity(city: String)
}
