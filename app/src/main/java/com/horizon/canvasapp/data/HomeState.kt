package com.horizon.canvasapp.data

import androidx.compose.ui.geometry.Size
import com.horizon.canvasapp.events.BallState

data class HomeState(
    val canvasSize: Size = Size.Zero,
    val ballSize: Map<Int, Size> = emptyMap(),
    val listBalls: List<BallState> = emptyList(),
    val selectedBall: Int? = null,
    val durationMillis: Int = 10000
)
