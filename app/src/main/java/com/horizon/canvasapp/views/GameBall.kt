package com.horizon.canvasapp.views

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.horizon.canvasapp.components.ViewsComponents.TopBarScreens
import com.horizon.canvasapp.data.HomeState
import com.horizon.canvasapp.events.BallState
import com.horizon.canvasapp.events.GameBallEvent
import com.horizon.canvasapp.viewmodel.GeneralVM

@Composable
fun GameBallRoute(navController: NavController, vm: GeneralVM) {

    val state by vm.state.collectAsState()
    val listBalls = listOf(
        "Download",
        "Mimatika",
        "Available in",
        "Google play",
    )

    //Initialized the function initBalls when UI charged
    LaunchedEffect(Unit) {
        vm.initBalls(listBalls)
    }

    Scaffold(
        topBar = {
            TopBarScreens(navController)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        //Finally, we draw the balls
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .onGloballyPositioned { layoutCoordinates ->
                    val size = layoutCoordinates.size.toSize()
                    vm.event(GameBallEvent.CanvasSize(size))
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            vm.event(GameBallEvent.OnStartDraw(offset))
                        },
                        onDrag = { _, dragAmount ->
                            vm.event(
                                GameBallEvent.OnMoveDraw(dragAmount)
                            )
                        },
                        onDragEnd = {
                            vm.event(GameBallEvent.OnStopDraw)
                        }
                    )
                }
        ) {
            state.listBalls.forEach { ballState ->
                val offsetX = ballState.position.x * state.canvasSize.width
                val offsetY = ballState.position.y * state.canvasSize.height
                GameBallExercise(
                    state,
                    vm,
                    ballState,
                    offsetX,
                    offsetY
                )
            }
        }
    }
}

@Composable
fun GameBallExercise(
    state: HomeState,
    vm: GeneralVM,
    ballState: BallState,
    offsetX: Float,
    offsetY: Float
) {

    val density = LocalDensity.current
    val size = remember(state.canvasSize) {
        with(density) { (state.canvasSize.width * .06f).toDp() }
    }

    Card(
        modifier = Modifier
            .size(size)
            .onGloballyPositioned { layoutCoordinates ->
                vm.event(
                    GameBallEvent.BallSize(
                        ballState.id,
                        size = layoutCoordinates.size.toSize()
                    )
                )
            }
            .offset {
                IntOffset(
                    x = (offsetX - (size + 8.dp).toPx() / 2).toInt(),
                    y = (offsetY - (size + 8.dp).toPx() / 2).toInt()
                )
            },
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
    ) {

    }


}