package com.romnan.chillax.presentation.composable.sounds.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.model.CategoryPresentation
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.util.asString

@Composable
fun CategoryItem(
    category: () -> CategoryPresentation,
    soundActiveBgColor: () -> Color,
    onSoundClick: (sound: SoundPresentation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = category().readableName.asString(),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
            color = MaterialTheme.colors.onSurface,
        )

        Text(
            text = category().description.asString(),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.small)) }

            items(
                count = category().sounds.size,
                key = { i -> category().sounds[i].name },
            ) { i ->
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                SoundItem(
                    sound = { category().sounds[i] },
                    selectedColor = { soundActiveBgColor() },
                    onClick = { onSoundClick(category().sounds[i]) },
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            }

            item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.small)) }
        }
    }
}