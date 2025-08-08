package com.oam.weatherapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oam.weatherapp.presentation.uistate.WeatherUiState
import com.oam.weatherapp.presentation.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeather("London") // For testing, replace with user input
    }

    when (uiState) {
        is WeatherUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is WeatherUiState.Success -> {
            val data = (uiState as WeatherUiState.Success).weatherInfo
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("City: ${data.cityName}", style = MaterialTheme.typography.headlineMedium)
                Text("Temperature: ${data.temperature}°C", style = MaterialTheme.typography.bodyLarge)
//                Text("Condition: ${data.condition}", style = MaterialTheme.typography.bodyLarge)
            }
        }

        is WeatherUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = (uiState as WeatherUiState.Error).message,
                    color = Color.Red
                )
            }
        }
    }
}
