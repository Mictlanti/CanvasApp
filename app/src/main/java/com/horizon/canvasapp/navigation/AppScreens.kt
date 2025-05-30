package com.horizon.canvasapp.navigation

sealed class AppScreens(val route: String) {
    //we create a sealed class with the routes of the screens
    data object HomeRoute : AppScreens("HomeView")
    data object GalaxyRoute : AppScreens("GalaxyView")
    data object ZoomRoute : AppScreens("ZoomView")
    data object SolarSystemRoute : AppScreens("SolarSystemView")
    data object FinalScreenRoute : AppScreens("FinalScreenView")
}
