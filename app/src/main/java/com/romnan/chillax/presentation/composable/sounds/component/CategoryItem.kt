package com.romnan.chillax.presentation.composable.sounds.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.model.CategoryPresentation
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.util.asString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryItem(
    category: () -> CategoryPresentation,
    soundActiveBgColor: () -> Color,
    onClickSound: (soundId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = category().readableName.asString(),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
        )

        Text(
            text = category().description.asString(),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.small)) }

            items(
                items = category().sounds,
                key = { sound: SoundPresentation -> sound.id },
            ) { sound: SoundPresentation ->
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                SoundItem(
                    sound = { sound },
                    selectedColor = { soundActiveBgColor() },
                    onClick = { onClickSound(sound.id) },
                    modifier = Modifier.animateItemPlacement(),
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            }

            item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.small)) }
        }
    }
}