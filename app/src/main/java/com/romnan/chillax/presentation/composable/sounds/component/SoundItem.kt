package com.romnan.chillax.presentation.composable.sounds.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.util.asString

@Composable
fun SoundItem(
    sound: SoundPresentation,
    activeBgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colors.surface)
            .width(100.dp)
            .clickable { onClick() },
    ) {
        val bgAlpha = animateFloatAsState(
            targetValue = if (sound.isSelected) 1f
            else 0f
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(activeBgColor.copy(alpha = bgAlpha.value))
                .aspectRatio(1f)
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = sound.iconResId),
                tint = MaterialTheme.colors.onSurface,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }

        Divider(modifier = Modifier.fillMaxWidth())

        Text(
            text = sound.readableName.asString(),
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.small),
        )
    }
}