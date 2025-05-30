package com.horizon.canvasapp.views

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.horizon.canvasapp.components.ViewsComponents.Star
import com.horizon.canvasapp.components.ViewsComponents.TopBarScreens
import com.horizon.canvasapp.components.ViewsComponents.glowingStarCanvas
import kotlin.math.pow
import kotlin.random.Random

@Composable
fun GalaxyRoute(navController: NavController) {
    Scaffold(
        topBar = {
            TopBarScreens(navController)
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Galaxy(paddingValues)
    }
}

@Composable
fun Galaxy(paddingValues: PaddingValues) {

    val start = List(20) {
        Star(
            posX = Random.nextFloat(), //Pos random in X
            posY = Math.random().toFloat(), //Pos random in Y
            size = 5.dp + (Random.nextFloat().pow(2) * 10).dp //Size random
        )
    }
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val color by infiniteTransition.animateColor(
        initialValue = Color.Yellow,
        targetValue = Color.Yellow.copy(alpha = 0.3f),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        start.forEach { s ->
            glowingStarCanvas(s, color)
        }
    }

}