package com.oam.weatherapp.di

import com.oam.weatherapp.data.remote.WeatherApi
import com.oam.weatherapp.data.repository.WeatherRepositoryImpl
import com.oam.weatherapp.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi
    ): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }
}
