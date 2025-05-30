package com.horizon.canvasapp.components.homeComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.horizon.canvasapp.viewmodel.GeneralVM

@Composable
fun CardNavigationHome(
    vm: GeneralVM,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val state by vm.state.collectAsState()
    //We get the size of the canvas and calculate the size of the cards
    //Very important for low or high resolution devices
    val density = LocalDensity.current
    val cardSize = remember(state.canvasSize) {
        with(density) { (state.canvasSize.width * .2f).toDp() }
    }

    Box(
        modifier = modifier
            .size(cardSize + 20.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .size(cardSize + 10.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        ) { }
        OutlinedCard(
            modifier = Modifier
                .size(cardSize),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
        ) { }
    }
}