package com.horizon.canvasapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.horizon.canvasapp.viewmodel.GeneralVM
import com.horizon.canvasapp.views.FinalScreenRoute
import com.horizon.canvasapp.views.GalaxyRoute
import com.horizon.canvasapp.views.HomeViewRoute
import com.horizon.canvasapp.views.GameBallRoute
import com.horizon.canvasapp.views.SolarSystemRoute

@Composable
fun NavigateApp(vm: GeneralVM) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeRoute.route
    ) {
        composable(AppScreens.HomeRoute.route) {
            HomeViewRoute(vm, navController)
        }
        composable(AppScreens.GalaxyRoute.route) {
            GalaxyRoute(navController)
        }
        composable(AppScreens.ZoomRoute.route) {
            GameBallRoute(navController, vm)
        }
        composable(AppScreens.SolarSystemRoute.route) {
            SolarSystemRoute(navController, vm)
        }

        composable(AppScreens.FinalScreenRoute.route) {
            FinalScreenRoute()
        }
    }
}