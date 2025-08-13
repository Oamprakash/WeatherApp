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
import kotlin.collections.firstOrNull

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
    private val CACHE_DURATION = 60 * 60 * 1000L // 1 hour in milliseconds


    override suspend fun getWeatherData(city: String): Flow<WeatherInfo> = flow {
        // Try reading from DB first
        val cached = weatherDao.getWeatherForCity(city).firstOrNull()?.toDomain()
        if (cached != null )
            emit(cached)
            println("oam cached weather $cached")
            try {
                apiKey.let { key ->
                    api.getWeatherByCity(city, key)
                        .run { // 'this' inside run refers to the response
                            main.temp.let { temp ->
                                name.let { cityName ->
                                    val weatherDescription =
                                        weather.firstOrNull()?.description ?: ""
//                        emit(
                                    val weatherInfo = WeatherInfo(
                                        temperature = temp,
                                        description = weatherDescription,
                                        cityName = cityName,
                                        iconUrl = weather.firstOrNull()?.icon?.let { iconId ->
                                            "https://openweathermap.org/img/wn/$iconId@2x.png" // Example URL structure
                                        } ?: "",
                                        lastUpdated = System.currentTimeMillis() // Default or placeholder if icon is not available
                                    )
//                        )
                                    emit(weatherInfo)
                                    weatherDao.insertWeather(weatherInfo.toEntity(cityName))
                                }
                            }
                        }
                }
            } catch (e: Exception) {
                if (cached == null) {
//                emit(e.localizedMessage ?:"Could not fetch forecast")
                    e.localizedMessage
                }

    }



    }

    // New: get forecast with caching
    override suspend fun getForecast(city: String): Flow<Resource<List<ForecastDay>>> = flow {
        // 1️⃣ Emit loading state
//        emit(Resource.Loading())

        // 2️⃣ Get cached data first
        val cachedData = weatherDao.getForecastForCity(city)
            .firstOrNull()
            ?.map { it.toDomain() }

        if (cachedData != null )
            emit(Resource.Success(cachedData)) // Show cached immediately
            println("oam cached $cachedData")


            // 3️⃣ Try fetching fresh data from API
            try {
                val dto = api.get5DayForecast(
                    city = city,
                    apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
                    units = "metric"
                )
                val domainList = dto.toDomainList()

                // 4️⃣ Save to DB
//            weatherDao.clearForecastForCity(city)
                weatherDao.insertForecast(domainList.map {
                    it.toEntity(
                        city,
                        System.currentTimeMillis()
                    )
                })

                // 5️⃣ Emit fresh data
                emit(Resource.Success(domainList))

                val cachedData = weatherDao.getForecastForCity(city)
                    .firstOrNull()
                    ?.map { it.toDomain() }
                println("oam cached2 $cachedData")

            } catch (e: Exception) {
                // 6️⃣ If network fails and no cached data was emitted, show error
//                if (cachedData.isNullOrEmpty()) {
//                    emit(Resource.Error(e.localizedMessage ?: "Could not fetch forecast"))
//                }
                e.localizedMessage
            }

    }


    // also expose a flow backed by DB for live updates (if desired)
    override fun getCachedForecastFlow(city: String): Flow<List<ForecastDay>> {
        return weatherDao.getForecastForCity(city).map { list -> list.map { it.toDomain() } }
    }

    private fun isCacheValid(lastUpdated: Long): Boolean {
        return System.currentTimeMillis() - lastUpdated < CACHE_DURATION
    }

}
