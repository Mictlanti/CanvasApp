package com.horizon.canvasapp.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.horizon.canvasapp.canvas.SolarSystemCanva
import com.horizon.canvasapp.components.ViewsComponents.TopBarScreens
import com.horizon.canvasapp.viewmodel.GeneralVM

@Composable
fun SolarSystemRoute(navController: NavController, vm: GeneralVM) {

    val planets = listOf(
        "Mercury",
        "Venus",
        "Earth",
        "Mars",
        "Jupiter",
        "Saturn",
        "Uranus",
        "Neptune"
    )
    val angularSpeeds = listOf(
        90f,  // Mercurio (rápido)
        70f,  // Venus
        50f,  // Tierra
        40f,  // Marte
        25f,  // Júpiter
        20f,  // Saturno
        15f,  // Urano
        10f   // Neptuno (lento)
    )
    val elapsedTime = remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        var startTime = 0L
        while (true) {
            withFrameNanos { frameTime ->
                if (startTime == 0L) startTime = frameTime
                elapsedTime.value = frameTime - startTime
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarScreens(navController)
        }
    ) { paddingValues ->
        Galaxy(paddingValues)
        SolarSystemCanva(
            Modifier.fillMaxSize(),
            planets,
            angularSpeeds,
            elapsedTime.value
        )
    }
}

