package com.oam.weatherapp.data.local.dao// data/local/dao/ForecastDao.kt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oam.weatherapp.data.local.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM forecast_table WHERE city = :city ORDER BY timestamp ASC")
    fun getForecastForCity(city: String): Flow<List<ForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(items: List<ForecastEntity>)

    @Query("DELETE FROM forecast_table WHERE city = :city")
    suspend fun clearForecastForCity(city: String)
}
