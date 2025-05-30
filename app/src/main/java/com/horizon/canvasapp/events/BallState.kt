package com.horizon.canvasapp.events

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class BallState(
    val id : Int,
    val position : Offset,
    val color : Color,
)