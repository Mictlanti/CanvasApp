package com.horizon.canvasapp.events

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

sealed class GameBallEvent {
    data class OnStartDraw(val offset: Offset) : GameBallEvent()
    data class OnMoveDraw(val dragAmount: Offset) : GameBallEvent()
    data object OnStopDraw : GameBallEvent()
    data class CanvasSize(val size: Size) : GameBallEvent()
    data class BallSize(val id: Int, val size: Size) : GameBallEvent()
}