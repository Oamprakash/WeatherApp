package com.oam.weatherapp.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.oam.weatherapp.domain.model.ForecastDay
import com.oam.weatherapp.util.Resource

// presentation/weather/ForecastChart.kt
@Composable
fun ForecastChart(
    forecast: List<ForecastDay>,
    modifier: Modifier = Modifier
) {
    if (forecast.isEmpty()) {
        Text("No forecast data", Modifier.padding(16.dp))
        return
    }

    val temps = forecast.map { it.temperature.toFloat() }
    val min = temps.minOrNull() ?: 0f
    val max = temps.maxOrNull() ?: 0f
    val range = (max - min).takeIf { it > 0 } ?: 1f

    val padding = 32f
    val pointRadius = 6f

    Canvas(modifier = modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(8.dp)
    ) {
        val w = size.width
        val h = size.height

        val stepX = w / (temps.size - 1).coerceAtLeast(1)

        // function to convert temp->y
        fun yFor(temp: Float): Float {
            val normalized = (temp - min) / range // 0..1
            // invert y: 0 -> top, 1 -> bottom
            return (h - padding) - normalized * (h - 2 * padding)
        }

        // points
        val points = temps.mapIndexed { i, t ->
            Offset(x = i * stepX, y = yFor(t))
        }

        // draw polyline
        val path = Path().apply {
            if (points.isNotEmpty()) {
                moveTo(points.first().x, points.first().y)
                for (p in points.drop(1)) lineTo(p.x, p.y)
            }
        }
        drawPath(path = path, color = Color(0xFF1E88E5), style = Stroke(width = 3f))
        // draw points
        points.forEach { p ->
            drawCircle(Color.White, radius = pointRadius + 2, center = p)
            drawCircle(Color(0xFF1E88E5), radius = pointRadius, center = p)
        }
        // draw labels for x-axis (every nth)
        val labelPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = 28f
            color = android.graphics.Color.DKGRAY
        }
        val labelFormatter = java.text.SimpleDateFormat("MMM d\nHH:mm", java.util.Locale.getDefault())
        forecast.forEachIndexed { i, item ->
            if (i % ((forecast.size / 4).coerceAtLeast(1)) == 0) {
                val px = i * stepX
                drawContext.canvas.nativeCanvas.drawText(
                    labelFormatter.format(java.util.Date(item.timestamp * 1000)),
                    px,
                    h - 4f,
                    labelPaint
                )
            }
        }

       /* when (val f = viewModel.forecastState.collectAsState().value) {
            is Resource.Loading -> CircularProgressIndicator()
            is Resource.Error -> Text("Forecast error: ${f.message}")
            is Resource.Success -> {
                ForecastChart(f.data ?: emptyList())
                // optionally show a horizontal list of forecast cards
            }
        }*/

    }
}
