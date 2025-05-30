package com.horizon.canvasapp.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SolarSystemCanva(
    modifier: Modifier = Modifier,
    planets: List<String>,
    angularSpeeds: List<Float>,
    elapsedTime: Long = 0L
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        Soon(color = Color.Yellow, w = w)

        val elapsedSeconds = elapsedTime / 1_000_000_000f

        planets.forEachIndexed { index, _ ->
            val angle = (elapsedSeconds * angularSpeeds[index]) % 360
            val radians = Math.toRadians(angle.toDouble())
            val radius = 150f + index * 120f
            val x = (w / 2 + radius * cos(radians)).toFloat()
            val y = (h / 2 + radius * sin(radians)).toFloat()
            val color = when(index) {
                0 -> 0xFFB0B0B0
                1 -> 0xFFEEDC82
                2 -> 0xFF2E74B5
                3 -> 0xFFB22222
                4 -> 0xFFD2B48C
                5 -> 0xFFF5DEB3
                6 -> 0xFFAFEEEE
                7 -> 0xFF4169E1
                else -> 0xFF000000
            }

            planetsSolar(
                color = Color(color),
                w = w,
                size = when (index) {
                    1 -> 3
                    2 -> 2
                    4 -> 1
                    5 -> 2
                    else -> 4
                },
                translation = Offset(x, y)
            )
        }
    }
}

private fun DrawScope.Soon(
    color: Color,
    w: Float
) {
    drawCircle(
        color = color,
        radius = (w * .1f) / 1,
        center = center
    )
}

private fun DrawScope.planetsSolar(
    color: Color,
    w: Float,
    size: Int,
    translation: Offset,
    index: Int = 0
) {
    drawCircle(
        color = color,
        radius = (w * .1f) / (size + 1),
        center = translation
    )
}