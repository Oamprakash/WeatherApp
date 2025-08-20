package com.oam.weatherapp.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.oam.weatherapp.domain.repository.WeatherRepository
import com.oam.weatherapp.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val weatherRepository: WeatherRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val city = inputData.getString("city") ?: return Result.failure()
            val apiKey = inputData.getString("apiKey") ?: return Result.failure()

            // Trigger repository fetch (this saves to Room)
            weatherRepository.getForecast(city).collect {
                // collect once to trigger network + DB update
                if (it is Resource.Success) {
                    Result.success()
                    print("oam success $Result.success()")
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry() // retry later if failure
        }
    }
}