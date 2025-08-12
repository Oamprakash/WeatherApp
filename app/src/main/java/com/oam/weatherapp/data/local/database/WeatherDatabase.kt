package com.oam.weatherapp.data.local.database// data/local/WeatherDatabase.kt

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oam.weatherapp.data.local.dao.WeatherDao
import com.oam.weatherapp.data.local.entity.ForecastEntity

@Database(entities = [ForecastEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val weatherDao: WeatherDao
}
