package com.oam.weatherapp.presentation.screen

import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.presentation.uistate.WeatherUiState
import com.oam.weatherapp.presentation.viewmodel.WeatherViewModel
import com.oam.weatherapp.util.Resource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val forecastRes by viewModel.forecastState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            label = { Text("Enter city name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.loadWeather(searchQuery) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is WeatherUiState.Loading -> Text("Loading...")
            is WeatherUiState.Error ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (uiState as WeatherUiState.Error).message,
                        color = Color.Red
                    )
                }

            is WeatherUiState.Success -> {
                Text("City: ${(uiState as WeatherUiState.Success).weatherInfo.cityName}")
                Text("Temperature: ${(uiState as WeatherUiState.Success).weatherInfo.temperature}°C")
                Text("Condition: ${(uiState as WeatherUiState.Success).weatherInfo.description}")
            }
        }

        // Forecast section + chart
        Text(text = "Forecast", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        when (forecastRes) {
            is Resource.Loading -> {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Success -> {
                val list = (forecastRes as Resource.Success).data ?: emptyList()
                ForecastChart(forecast = list, modifier = Modifier.fillMaxWidth().height(200.dp))
                Spacer(modifier = Modifier.height(8.dp))
                // Optionally show simple list of forecast items
                list.forEach { item ->
                    ForecastRow(item)
                }
            }

            is Resource.Error -> {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Forecast error: ${(forecastRes as Resource.Error).message}",
                        color = Color.Red
                    )
                }
            }
        }
    }


    }

@Composable
private fun ForecastRow(item: ForecastDay) {
    val sdf = remember { SimpleDateFormat("EEE, MMM d HH:mm", Locale.getDefault()) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = sdf.format(Date(item.timestamp * 1000)))
        Text(text = "${item.temperature}°C")
        Text(text = item.description)
    }
}

