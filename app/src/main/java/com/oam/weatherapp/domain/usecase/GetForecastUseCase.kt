package com.oam.weatherapp.domain.usecase// domain/usecase/GetForecastUseCase.kt

import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.domain.repository.WeatherRepository
import com.oam.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repositoryImpl: WeatherRepository // or inject interface if you changed it
) {
    suspend operator fun invoke(city: String): Flow<Resource<List<ForecastDay>>> {
        return repositoryImpl.getForecast(city)
    }
}
