package com.romnan.chillax.presentation.composable.moods.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.util.asString

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoodItem(
    mood: () -> Mood,
    isPlaying: Boolean,
    onClickDelete: ((Mood) -> Unit),
    onClickPlayOrPause: ((Mood) -> Unit),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(mood().imageUri)
                .scale(Scale.FILL)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth(),
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(MaterialTheme.spacing.extraSmall)
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colors.background.copy(alpha = 0.8f))
                .padding(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

            Text(
                text = context.resources.getQuantityString(
                    R.plurals.n_sounds,
                    mood().soundIdToVolume.count(),
                    mood().soundIdToVolume.count().toString(),
                ),
                style = MaterialTheme.typography.caption,
            )
        }

        if (mood().isCustom) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(MaterialTheme.spacing.extraSmall)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(100),
                        clip = true,
                    )
                    .background(MaterialTheme.colors.background.copy(alpha = 0.8f))
                    .padding(MaterialTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentEnforcement provides false
                ) {
                    IconButton(onClick = { onClickDelete(mood()) }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(MaterialTheme.spacing.extraSmall)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(100),
                    clip = true,
                )
                .clickable { onClickPlayOrPause(mood()) }
                .background(MaterialTheme.colors.background.copy(alpha = 0.8f))
                .padding(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = mood().readableName.asString(),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = MaterialTheme.spacing.small),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
            )

            Icon(
                imageVector = when (isPlaying) {
                    true -> Icons.Filled.PauseCircle
                    false -> Icons.Filled.PlayCircle
                },
                contentDescription = stringResource(
                    R.string.cd_play_x,
                    mood().readableName.asString()
                ),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}