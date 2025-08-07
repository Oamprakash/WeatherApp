# 🌤️ WeatherApp

A modern Android Weather App built with **Jetpack Compose**, **Kotlin**, and **Clean Architecture**. It fetches real-time weather data using the [OpenWeatherMap API](https://openweathermap.org/api).

## 📱 Features

- 🔍 Search weather by city name
- 📍 Get current location weather
- 🌡️ Displays:
  - Temperature
  - Weather condition (clear, rain, etc.)
  - Humidity
  - Wind speed
- 🧭 Clean MVVM + Repository architecture
- 🧪 Kotlin Coroutines and Flow
- 💅 Jetpack Compose UI

---

## 📸 Screenshots

> (To be added)

---

## 🧱 Tech Stack

| Layer          | Tech Used |
|----------------|-----------|
| UI             | Jetpack Compose, Material 3 |
| Architecture   | MVVM + Clean Architecture |
| Network        | Retrofit, Gson |
| Dependency Injection | Hilt |
| Async          | Kotlin Coroutines, Flow |
| Location       | Fused Location Provider |
| API            | OpenWeatherMap |

---

## 🔧 Setup Instructions

1. **Clone the repo**
   ```bash
   git clone https://github.com/oamprakash/WeatherApp.git
   cd WeatherApp
Open in Android Studio

Api key - 826090f23332db55d06a5298be4d4d02

Get one from https://openweathermap.org/api

Add it to local.properties:

properties

OPEN_WEATHER_API_KEY="826090f23332db55d06a5298be4d4d02"


📂 Architecture Overview

📂 presentation  # UI Layer (Jetpack Compose, ViewModel)
📂 domain        # UseCases, Repository interfaces, Data models
📂 data          # Retrofit API, DTOs, Repositories
Clean Architecture with separation of concerns

📄 License
This project is licensed under the MIT License.

✨ Credits
OpenWeatherMap API

Jetpack Compose by Google
