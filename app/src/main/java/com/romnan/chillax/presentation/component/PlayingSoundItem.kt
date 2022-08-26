package com.romnan.chillax.presentation.component

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
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.presentation.theme.spacing

@Composable
fun PlayingSoundItem(
    sound: Sound,
    onVolumeChange: (level: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .height(36.dp)
            .clip(shape = RoundedCornerShape(size = 12.dp))
    ) {
        var volumeLevel by remember { mutableStateOf(0.5f) }

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.secondary.copy(alpha = 0.3f)),
        ) {
            if (volumeLevel > 0.0f) Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(volumeLevel)
                    .background(MaterialTheme.colors.secondary)
            )
        }

        Slider(
            value = volumeLevel,
            onValueChange = {
                onVolumeChange(it)
                volumeLevel = it
            },
            modifier = Modifier
                .fillMaxSize()
                .alpha(0f),
        )

        Icon(
            painter = painterResource(id = sound.iconResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(MaterialTheme.spacing.small),
        )
    }
}