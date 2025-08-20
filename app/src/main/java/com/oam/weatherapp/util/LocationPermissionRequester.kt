package com.oam.weatherapp.util

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionRequester(
    onPermissionGranted: () -> Unit
) {
    val locationPermission = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        locationPermission.launchPermissionRequest()
    }

    when {
        locationPermission.status.isGranted -> {
            onPermissionGranted()
        }
        locationPermission.status.shouldShowRationale -> {
            Text("Location permission is needed to show weather for your area.")
        }
        else -> {
            Text("Grant location permission in settings to continue.")
        }
    }
}
