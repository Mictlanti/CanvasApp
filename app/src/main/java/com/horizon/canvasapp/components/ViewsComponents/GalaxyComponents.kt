package com.horizon.canvasapp.components.ViewsComponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Star(val posX: Float, val posY: Float, val size: Dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScreens(navController: NavController) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "Arrow Back",
                    tint = Color.White
                )
            }
        },
        title = {
            Text(
                text = "Galaxy",
                color = Color.White,
                fontSize = 30.sp
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black
        )
    )
}

fun DrawScope.glowingStarCanvas(star: Star, color: Color) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val starSizePx = star.size.toPx()

    val position = Offset(
        x = star.posX * canvasWidth,  // Convert relative position to absolute in X
        y = star.posY * canvasHeight  // Convert relative position to absolute in Y
    )

    //Drawing a Start
    val path = Path().apply {
        moveTo(position.x, position.y - starSizePx / 2)
        quadraticTo(
            position.x, position.y,
            position.x + starSizePx / 2, position.y
        )
        quadraticTo(
            position.x, position.y,
            position.x, position.y + starSizePx / 2
        )
        quadraticTo(
            position.x, position.y,
            position.x - starSizePx / 2, position.y
        )
        quadraticTo(
            position.x, position.y,
            position.x, position.y - starSizePx / 2
        )
    }

    drawPath(
        path = path,
        color = color,
        style = Fill
    )
}