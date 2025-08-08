package com.oam.weatherapp.data.repository

import com.oam.weatherapp.BuildConfig
import com.oam.weatherapp.data.remote.WeatherApi
import com.oam.weatherapp.domain.model.WeatherInfo
import com.oam.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Properties

class WeatherRepositoryImpl(
    private val api: WeatherApi
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
}
