package com.oam.weatherapp.data.local.database// data/local/WeatherDatabase.kt

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oam.weatherapp.data.local.dao.WeatherDao
import com.oam.weatherapp.data.local.entity.ForecastEntity
import com.oam.weatherapp.data.local.entity.WeatherEntity

@Database(entities = [ForecastEntity::class, WeatherEntity::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val weatherDao: WeatherDao
}
