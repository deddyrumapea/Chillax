package com.romnan.chillax.presentation.composable.sounds.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val color by animateColorAsState(
        if (sound().isPlaying) selectedColor()
        else MaterialTheme.colorScheme.surface,
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        tonalElevation = 2.dp,
        modifier = modifier,
        onClick = onClick,
        color = color,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(100.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth(),
            ) {
                AsyncImage(
                    model = sound().iconResId,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.size(24.dp),
                )
            }

            Surface {
                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                Text(
                    text = sound().readableName.asString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.small),
                )
            }
        }
    }
}