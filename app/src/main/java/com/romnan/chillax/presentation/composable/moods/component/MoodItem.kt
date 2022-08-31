package com.romnan.chillax.presentation.composable.moods.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.model.MoodPresentation
import com.romnan.chillax.presentation.util.asString

@Composable
fun MoodItem(
    mood: MoodPresentation,
    onClick: (mood: MoodPresentation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .padding(MaterialTheme.spacing.small)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(mood) }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(mood.imageResId)
                .scale(Scale.FILL)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colors.surface.copy(alpha = 0.2f),
                            MaterialTheme.colors.surface,
                        )
                    )
                )
                .padding(
                    vertical = MaterialTheme.spacing.small,
                    horizontal = MaterialTheme.spacing.medium,
                )
        ) {
            // TODO: create plural string formatter
            Text(
                text = "${mood.sounds.size} sounds",
                style = MaterialTheme.typography.caption,
            )
            Text(text = mood.readableName.asString())
        }
    }
}