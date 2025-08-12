package com.oam.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.oam.weatherapp.data.local.dao.WeatherDao
import com.oam.weatherapp.data.local.database.WeatherDatabase
import com.oam.weatherapp.data.remote.WeatherApi
import com.oam.weatherapp.data.repository.WeatherRepositoryImpl
import com.oam.weatherapp.domain.repository.WeatherRepository
import com.oam.weatherapp.domain.usecase.GetForecastUseCase
import com.oam.weatherapp.domain.usecase.GetWeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Bind WeatherRepository interface to its implementation
     */
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    companion object {

        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        /**
         * Provide Retrofit API service
         */
        @Provides
        @Singleton
        fun provideWeatherApi(): WeatherApi {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)
        }

        /**
         * Provide Room Database
         */
        @Provides
        @Singleton
        fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
            return Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        "weather_db"
                    ).fallbackToDestructiveMigration(true).build()
        }

        /**
         * Provide Weather DAO
         */
        @Provides
        @Singleton
        fun provideWeatherDao(db: WeatherDatabase): WeatherDao {
            return db.weatherDao
        }

        /**
         * Provide GetWeatherUseCase
         */
        @Provides
        @Singleton
        fun provideGetWeatherUseCase(
            repository: WeatherRepository
        ): GetWeatherUseCase {
            return GetWeatherUseCase(repository)
        }

        /**
         * Provide GetForecastUseCase
         */
        @Provides
        @Singleton
        fun provideGetForecastUseCase(
            repository: WeatherRepository
        ): GetForecastUseCase {
            return GetForecastUseCase(repository)
        }
    }
}
