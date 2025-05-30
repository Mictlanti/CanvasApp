package com.horizon.canvasapp.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.horizon.canvasapp.components.homeComponents.CardNavigationHome
import com.horizon.canvasapp.navigation.AppScreens
import com.horizon.canvasapp.viewmodel.GeneralVM

@Composable
fun HomeViewRoute(vm: GeneralVM, navController: NavController) {

    val positionCards = listOf(
        Offset(.7f, .05f),
        Offset(.45f, .2f),
        Offset(.3f, .4f),
        Offset(.45f, .6f),
        Offset(.7f, .8f)
    )
    val navigate = listOf(
        AppScreens.GalaxyRoute,
        AppScreens.ZoomRoute,
        AppScreens.SolarSystemRoute,
        AppScreens.FinalScreenRoute
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        HomeView(vm, paddingValues, positionCards, navController, navigate)
    }
}

@Composable
fun HomeView(
    vm: GeneralVM,
    paddings: PaddingValues,
    listCards: List<Offset>,
    navController: NavController,
    navigate: List<AppScreens>
) {
    //Our list of cards positions
    val state by vm.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                vm.updateCanvasSize(layoutCoordinates.size.toSize())
            }
            .padding(paddings)
    ) {
        //For the items in the list we create a card with a position
        listCards.forEachIndexed { index, offset ->
            CardNavigationHome(
                vm,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (state.canvasSize.width * offset.x).toInt(),
                            (state.canvasSize.height * offset.y).toInt()
                        )
                    }
            ) {
                //When we click on the cards, we navigate to the screens according to the index:
                navController.navigate(navigate[index].route)
            }
        }
    }
}