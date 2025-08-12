package com.oam.weatherapp.data.repository

import com.oam.weatherapp.BuildConfig
import com.oam.weatherapp.data.local.dao.WeatherDao
import com.oam.weatherapp.data.local.entity.toDomain
import com.oam.weatherapp.data.local.entity.toEntity
import com.oam.weatherapp.data.mapper.toDomainList
import com.oam.weatherapp.data.remote.WeatherApi
import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.domain.model.WeatherInfo
import com.oam.weatherapp.domain.repository.WeatherRepository
import com.oam.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val forecastDao: WeatherDao
) : WeatherRepository {

    val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
//    val apiKey: String? = Properties().getProperty("OPEN_WEATHER_API_KEY")

    override suspend fun getWeatherData(city: String): Flow<WeatherInfo> = flow {
        apiKey.let { key ->
            api.getWeatherByCity(city, key).run { // 'this' inside run refers to the response
                main.temp.let { temp ->
                    name.let { cityName ->
                        val weatherDescription = weather.firstOrNull()?.description ?: ""
                        emit(
                            WeatherInfo(
                                temperature = temp,
                                description = weatherDescription,
                                cityName = cityName,
                                iconUrl = weather.firstOrNull()?.icon?.let { iconId ->
                                    "https://openweathermap.org/img/wn/$iconId@2x.png" // Example URL structure
                                } ?: "" // Default or placeholder if icon is not available
                            )
                        )
                    }
                }
            }
        }
    }

    // New: get forecast with caching
    override suspend fun getForecast(city: String): Flow<Resource<List<ForecastDay>>> = flow {
        emit(Resource.Loading())

        try {
            // call remote
            val dto = api.get5DayForecast(city = city, apiKey = BuildConfig.OPEN_WEATHER_API_KEY, units = "metric")
            val domainList = dto.toDomainList()

            // save to DB (replace city's old forecast)
            forecastDao.clearForecastForCity(city)
            forecastDao.insertForecast(domainList.map { it.toEntity(city) })

            emit(Resource.Success(domainList))
        } catch (e: Exception) {
            // on error, try to return cached data
            val cached = forecastDao.getForecastForCity(city).firstOrNull()?.map { it.toDomain() }
            if (!cached.isNullOrEmpty()) {
                emit(Resource.Success(cached))
            } else {
                emit(Resource.Error(e.localizedMessage ?: "Could not fetch forecast"))
            }
        }
    }

    // also expose a flow backed by DB for live updates (if desired)
    override fun getCachedForecastFlow(city: String): Flow<List<ForecastDay>> {
        return forecastDao.getForecastForCity(city).map { list -> list.map { it.toDomain() } }
    }
}
