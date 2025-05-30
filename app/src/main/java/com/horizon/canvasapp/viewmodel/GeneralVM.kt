package com.horizon.canvasapp.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModel
import com.horizon.canvasapp.data.HomeState
import com.horizon.canvasapp.events.BallState
import com.horizon.canvasapp.events.GameBallEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GeneralVM : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    //We save the size of the canvas in the state
    fun updateCanvasSize(size: Size) {
        _state.value = _state.value.copy(canvasSize = size)
    }

    //We handle the events for GameBall
    fun event(event: GameBallEvent) {
        when (event) {
            is GameBallEvent.OnStartDraw -> {
                //We get the size of the canvas
                val canvasSize = _state.value.canvasSize

                // Convert the touch position x and y to a value between 0 and 1
                val touchPositionX = event.offset.x / canvasSize.width
                val touchPositionY = event.offset.y / canvasSize.height

                //For example: event.offset.x = 100, canvasSize.width = 200, touchPositionX = 0.5f
                //The values between 0 at 1 represent: 0% of the canvas to 100% of the canvas
                //In the example, the touch position is 50% of the canvas, so touchPositionX = 0.5f

                //We represent the touch position as an Offset
                val touchPos = Offset(
                    touchPositionX,
                    touchPositionY
                )

                //We search a ball in the list "listBalls" that satisfies the conditions:
                //The ".find" value will loop through each element until it finds the first element that satisfies the condition
                val selectedId = _state.value.listBalls.find { point ->

                    /*
                    Long explanation:
                    The Cards have a width an height, therefore they occupy a space in the canvas.
                    We need to calculate the size of the card in the canvas for define the size of the touch area.
                    Then:
                    If the user touch in the area of the card (val halfWidth = (cardSize.width / canvasSize.width) / 2 + touchMargin) the ball is selected, else return false.
                    Same for the height.
                    Example:
                    Canvas: 300px
                    Ball: 60px
                    touchMargin = .05f
                    cardSize.width / canvasSize.width = 60px / 300px = 0.2
                    halfWidth = .2 / 2 = .1
                    halfWidth + touchMargin = .1 + .05 = .15
                    then, the ball extends to 15% to left and right from its position.x.
                    In other words...
                    "touchPositionX" and "touchPositionY" detect the position of the touch in the canvas.
                    "halfWidth" and "halfHeight" detect if the touch is in the area of the card.
                    */

                    val cardSize = _state.value.ballSize[point.id] ?: return@find false // If the size is null, return false
                    val touchMargin = .05f

                    //We created a horizontal range, which goes from: Left (centerX - halfWidth) to Right (centerX + halfWidth)
                    val halfWidth = (cardSize.width / canvasSize.width) / 2 + touchMargin
                    val halfHeight = (cardSize.height / canvasSize.height) / 2 + touchMargin

                    //And, if touchPos.x is in this range, the touch was inside the card
                    touchPos.x in (point.position.x - halfWidth)..(point.position.x + halfWidth) &&
                            touchPos.y in (point.position.y - halfHeight)..(point.position.y + halfHeight)
                    //the same for the y axis

                }?.id //return the id of the ball that was selected

                _state.value = _state.value.copy(selectedBall = selectedId)

            }

            is GameBallEvent.OnMoveDraw -> {
                //Here we update the position of the ball that was selected
                _state.update { state ->
                    //First: We check if a ball was selected
                    state.selectedBall?.let { ballId ->
                        state.copy(
                            //Second: We map the list of balls, and if the ball is selected, we update its position
                            listBalls = state.listBalls.map { ball ->
                                if(ball.id == ballId) {
                                    //We calculate the new position of the ball. Takes the current position of the ball and adds the movement (dragAmount)
                                    //Then, divide by the canvas size to get the percentage of the canvas (0 to 1)
                                    val newPosition = Offset(
                                        x = ball.position.x + event.dragAmount.x / state.canvasSize.width,
                                        y = ball.position.y + event.dragAmount.y / state.canvasSize.height
                                    )

                                    //Here we limit the movement of the ball to the canvas
                                    //If the ball is outside the canvas, it will return to position 2% in Left or 98% Right of the Canvas
                                    val limitCanvasX = when(ball.position.x) {
                                        in -.05f..-.01f -> 0.02f
                                        in .98f..1.2f -> .98f
                                        else -> newPosition.x
                                    }

                                    //If the ball is outside the canvas, it will return to position 2% in Top or 101% Bottom of the Canvas
                                    val limitCanvasY = when(ball.position.y) {
                                        in -.05f..-.01f -> 0.02f
                                        in 1.02f..1.5f -> 1.01f
                                        else -> newPosition.y
                                    }

                                    //Finally, we update the position of the ball
                                    ball.copy(id = ballId, position = Offset(limitCanvasX, limitCanvasY))
                                } else ball //If the ball is not selected, we return it
                            }
                        )
                    } ?: state
                }
            }

            is GameBallEvent.OnStopDraw -> {
                _state.value = _state.value.copy(selectedBall = null)
            }

            is GameBallEvent.CanvasSize -> {
                //We refresh the canvas size
                _state.update { state ->
                    state.copy(canvasSize = Size.Zero)
                }
                //Update the canvas size
                _state.value = _state.value.copy(canvasSize = event.size)
            }

            is GameBallEvent.BallSize -> {
                _state.update {
                    it.copy(ballSize = it.ballSize + (event.id to event.size))
                }
            }

        }

    }

    //We created a function to add a ball to the list of balls
    private fun addBall(id: Int, newPosition: Offset, color: Color) {
        _state.update { state ->
            val newBall = BallState(
                id = id,
                position = newPosition,
                color = color
            )
            state.copy(
                listBalls = state.listBalls + newBall
            )
        }
    }

    //Then, we created a function to initialize the list of balls
    fun initBalls(listBalls: List<String>) {
        //We clear the list of balls
        _state.update { state -> state.copy(listBalls = emptyList())}

        //we choose a initial random position for the balls
        val randomX = List(listBalls.size) { Random.nextFloat() * (0.7f - 0.1f) + 0.1f }
        //For each item in the list, we create a random position between 0.1 and 0.7
        val randomY = List(listBalls.size) { Random.nextFloat() * (0.7f - 0.1f) + 0.1f }

        val randomColor = List(listBalls.size) { Random.nextFloat() * 360f } //Whatever tone of color we want

        //For each item in the list, we add a ball to the list of balls with an id, position and color
        listBalls.forEachIndexed { index, _ ->
            val colorInt = ColorUtils.HSLToColor(floatArrayOf(randomColor[index], 1f, 0.5f))
            addBall(
                id = index + 1,
                newPosition = Offset(randomX[index], randomY[index]),
                color = Color(colorInt)
            )
        }
    }

    fun updateDuration(duration: Int) {
        _state.update { state -> state.copy(durationMillis = duration) }
    }

}