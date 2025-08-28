package com.romnan.chillax.presentation.composable.moods

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.moods.component.MoodItem
import com.romnan.chillax.presentation.composable.moods.model.MoodType
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.util.asString

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Destination
@RootNavGraph(start = true)
fun MoodsScreen(
    viewModel: MoodsViewModel
) {
    val state by viewModel.state.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.small),
    ) {
        item(span = { GridItemSpan(currentLineSpan = 2) }) {
            ScreenTitle(
                text = { stringResource(id = R.string.moods) },
                paddingValues = PaddingValues(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.large,
                ),
            )
        }

        item(
            span = { GridItemSpan(currentLineSpan = 2) },
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.medium),
            ) {
                item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.small)) }

                items(
                    items = state.moodTypes,
                ) { moodType: MoodType ->
                    val moodTypeBgColor by animateColorAsState(
                        targetValue = when (moodType == state.selectedMoodType) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> MaterialTheme.colorScheme.surface
                        },
                    )

                    val moodTypeContentColor by animateColorAsState(
                        targetValue = when (moodType == state.selectedMoodType) {
                            true -> MaterialTheme.colorScheme.onPrimary
                            false -> MaterialTheme.colorScheme.onSurface
                        },
                    )

                    Surface(
                        onClick = { viewModel.onSelectMoodType(moodType) },
                        color = moodTypeBgColor,
                        shadowElevation = 2.dp,
                        shape = RoundedCornerShape(100),
                        tonalElevation = 2.dp,
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(
                                    vertical = MaterialTheme.spacing.small,
                                    horizontal = MaterialTheme.spacing.medium,
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = moodType.icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = moodTypeContentColor,
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                            Text(
                                text = moodType.label.asString(),
                                fontWeight = FontWeight.SemiBold,
                                color = moodTypeContentColor,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                }

                item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.small)) }
            }
        }

        item(
            span = { GridItemSpan(currentLineSpan = 2) },
        ) {
            AnimatedVisibility(
                visible = state.moods.isEmpty() && state.selectedMoodType == MoodType.Custom,
            ) {
                Text(
                    text = stringResource(R.string.it_s_empty_when_you_save_moods_they_will_show_up_here),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.spacing.small,
                            vertical = MaterialTheme.spacing.large,
                        ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
        }

        items(
            items = state.moods,
            key = { mood: Mood -> mood.id },
        ) { mood: Mood ->
            MoodItem(
                mood = { mood },
                isPlaying = state.player?.phase == PlayerPhase.PLAYING && state.player?.playingMood?.id == mood.id,
                onClickPlayOrPause = viewModel::onClickPlayOrPause,
                onClickDelete = viewModel::onClickDeleteMood,
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(MaterialTheme.spacing.small)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(20.dp),
                        clip = true,
                    ),
            )
        }

        item(span = { GridItemSpan(currentLineSpan = 2) }) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }

    state.customMoodToDelete?.let { mood: Mood ->
        AlertDialog(
            title = {
                Text(
                    text = stringResource(
                        R.string.are_you_sure_you_want_to_delete_x,
                        mood.readableName.asString(),
                    ),
                )
            },
            onDismissRequest = viewModel::onDismissDeleteMoodDialog,
            confirmButton = {
                Button(
                    onClick = { viewModel.onClickConfirmDeleteMood(mood = mood) },
                ) {
                    Text(
                        text = stringResource(
                            R.string.delete_x,
                            mood.readableName.asString(),
                        ),
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = viewModel::onDismissDeleteMoodDialog,
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            text = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = mood.imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(84.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
            },
        )
    }
}