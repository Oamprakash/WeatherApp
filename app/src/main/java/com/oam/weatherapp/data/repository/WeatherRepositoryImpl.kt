package com.oam.weatherapp.data.repository

import android.annotation.SuppressLint
import com.oam.weatherapp.BuildConfig
import com.oam.weatherapp.data.local.dao.WeatherDao
import com.oam.weatherapp.data.local.entity.ForecastEntity
import com.oam.weatherapp.data.local.entity.WeatherEntity
import com.oam.weatherapp.data.local.entity.toDomain
import com.oam.weatherapp.data.local.entity.toEntity
import com.oam.weatherapp.data.mapper.toDomainList
import com.oam.weatherapp.data.model.WeatherDto
import com.oam.weatherapp.data.remote.WeatherApi
import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.domain.model.WeatherInfo
import com.oam.weatherapp.domain.repository.WeatherRepository
import com.oam.weatherapp.util.CachePolicy
import com.oam.weatherapp.util.Resource
import com.oam.weatherapp.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.firstOrNull

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
    private val CACHE_DURATION = 60 * 60 * 1000L // 1 hour in milliseconds

    override suspend fun getWeatherData(city: String): Flow<Resource<WeatherEntity?>> {
        return networkBoundResource(
            query = { weatherDao.getWeatherForCity(city) },
            fetch = { weatherApi.getWeatherByCity(city, apiKey) },
            saveFetchResult = { dto ->
                val entity = WeatherEntity(
                    cityName = city,
                    temperature = dto.main.temp,
                    description = dto.weather.firstOrNull()?.description ?: "Unknown",
                    icon = dto.weather.firstOrNull()?.icon?.let { iconId ->
                        "https://openweathermap.org/img/wn/$iconId@2x.png" // Example URL structure
                    } ?: "",
                    lastUpdated = System.currentTimeMillis()
                )
                weatherDao.insertWeather(entity)
            },
            shouldFetch = { localData ->
                // Example: fetch if no data OR older than 30 min
                localData == null // can add time check later
            }
        )
    }

    override suspend fun getForecast(city: String): Flow<Resource<List<ForecastDay>>> {
        return networkBoundResource(
            query = {
                weatherDao.getForecastForCity(city).map { entities ->
                    entities.map { it.toDomain() }
                }
            },
            fetch = { weatherApi.get5DayForecast(city, apiKey) },
            saveFetchResult = { dto ->
                weatherDao.clearForecastForCity(city)
                val entities = dto.list.map {
                    ForecastEntity(
                        city = city,
                        timestamp = it.dt,
                        temperature = it.main.temp,
                        description = it.weather.firstOrNull()?.description ?: "Unknown",
                        icon = it.weather.firstOrNull()?.icon,
                        lastUpdated = System.currentTimeMillis()
                    )
                }
                weatherDao.insertForecast(entities)
            },
            shouldFetch = { localData ->
                // Example rule: refresh if no forecast OR data is older than 30 minutes
                localData.isNullOrEmpty() ||
                        localData.any { System.currentTimeMillis() - it.lastUpdated > 30 * 60 * 1000 }
            }
        )
    }

    // also expose a flow backed by DB for live updates (if desired)
    override fun getCachedForecastFlow(city: String): Flow<List<ForecastDay>> {
        return weatherDao.getForecastForCity(city).map { list -> list.map { it.toDomain() } }
    }

    private fun isCacheValid(lastUpdated: Long): Boolean {
        return System.currentTimeMillis() - lastUpdated < CACHE_DURATION
    }

}
