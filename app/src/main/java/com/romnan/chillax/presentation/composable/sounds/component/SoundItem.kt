package com.romnan.chillax.presentation.composable.sounds.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.util.asString

@Composable
fun SoundItem(
    sound: () -> SoundPresentation,
    selectedColor: () -> Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true,
            )
            .background(color = MaterialTheme.colors.surface)
            .width(100.dp)
            .clickable { onClick() },
    ) {
        val boxColor = animateColorAsState(
            if (sound().isPlaying) selectedColor()
            else MaterialTheme.colors.surface,
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .drawBehind { drawRect(color = boxColor.value) }
                .aspectRatio(1f)
                .fillMaxWidth()
        ) {
            val iconColor = animateColorAsState(
                if (sound().isPlaying) MaterialTheme.colors.onPrimary
                else MaterialTheme.colors.onSurface
            )

            AsyncImage(
                model = sound().iconResId,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = iconColor.value),
                modifier = Modifier.size(24.dp),
            )
        }

        Divider(modifier = Modifier.fillMaxWidth())

        Text(
            text = sound().readableName.asString(),
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.small),
        )
    }
}