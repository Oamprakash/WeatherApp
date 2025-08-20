package com.oam.weatherapp.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.oam.weatherapp.BuildConfig
import com.oam.weatherapp.data.work.WeatherSyncWorker
import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.presentation.uistate.WeatherUiState
import com.oam.weatherapp.presentation.viewmodel.WeatherViewModel
import com.oam.weatherapp.util.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val forecastRes by viewModel.forecastState.collectAsState()
    val apiKey = BuildConfig.OPEN_WEATHER_API_KEY

    val context = LocalContext.current

    // Launcher for multiple permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineGranted || coarseGranted) {
            // Safe to fetch location now
//            viewModel.loadWeatherForCurrentLocation()
        } else {
            // Show rationale / denied state UI
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Run once when the screen loads
    LaunchedEffect(Unit) {
        val finePermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarsePermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (finePermission == PackageManager.PERMISSION_GRANTED ||
            coarsePermission == PackageManager.PERMISSION_GRANTED
        ) {
            // Already granted
//            viewModel.loadWeatherForCurrentLocation()
        } else {
            // Request both permissions
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    /*LaunchedEffect(Unit) {
        val workRequest = PeriodicWorkRequestBuilder<WeatherSyncWorker>(
            1, TimeUnit.MINUTES
        )
            .setInputData(
                workDataOf("city" to "Chennai", "apiKey" to apiKey)
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "WeatherSync",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }*/


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
                Text("City: ${(uiState as WeatherUiState.Success).weatherInfo?.cityName}")
                Text("Temperature: ${(uiState as WeatherUiState.Success).weatherInfo?.temperature}°C")
                Text("Condition: ${(uiState as WeatherUiState.Success).weatherInfo?.description}")
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
                ForecastChart(
                    forecast = list,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
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

