package com.oam.weatherapp.util

// util/CachePolicy.kt
object CachePolicy {
    const val WEATHER_TTL_MS = 10 * 60 * 1000L  // 10 min
    const val FORECAST_TTL_MS = 30 * 60 * 1000L // 30 min

    fun isExpired(lastUpdatedAt: Long, ttlMs: Long): Boolean =
        System.currentTimeMillis() - lastUpdatedAt > ttlMs
}
