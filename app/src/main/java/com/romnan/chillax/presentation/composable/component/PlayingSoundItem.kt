package com.romnan.chillax.presentation.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.model.SoundPresentation

@Composable
fun PlayingSoundItem(
    sound: () -> SoundPresentation,
    onVolumeChange: (volume: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.secondary.copy(alpha = 0.3f))
            .height(36.dp),
    ) {
        var volumeState by remember(key1 = sound().name) { mutableStateOf(value = sound().volume) }

        if (volumeState > 0f) Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = volumeState)
                .background(MaterialTheme.colors.secondary)
        )

        Slider(
            value = volumeState,
            onValueChange = {
                volumeState = it
                onVolumeChange(volumeState)
            },
            modifier = Modifier
                .fillMaxSize()
                .alpha(0f),
        )

        Icon(
            painter = painterResource(id = sound().iconResId),
            tint = MaterialTheme.colors.onSecondary,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(MaterialTheme.spacing.small),
        )
    }
}