package com.romnan.chillax.presentation.composable.moods

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.presentation.composable.component.DefaultDialog
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
    val scaffoldState = rememberScaffoldState()

    val state by viewModel.state.collectAsState()

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
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
                                true -> MaterialTheme.colors.primary
                                false -> MaterialTheme.colors.surface
                            },
                        )

                        val moodTypeContentColor by animateColorAsState(
                            targetValue = when (moodType == state.selectedMoodType) {
                                true -> MaterialTheme.colors.onPrimary
                                false -> MaterialTheme.colors.onSurface
                            },
                        )

                        Row(
                            modifier = Modifier
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(100),
                                    clip = true,
                                )
                                .drawBehind { drawRect(moodTypeBgColor) }
                                .clickable { viewModel.onSelectMoodType(moodType) }
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
                            )
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
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
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
            DefaultDialog(
                title = {
                    stringResource(
                        R.string.delete_x,
                        mood.readableName.asString(),
                    )
                },
                onDismissRequest = viewModel::onDismissDeleteMoodDialog,
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Text(
                    text = stringResource(
                        R.string.are_you_sure_you_want_to_delete_x,
                        mood.readableName.asString(),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                Button(
                    onClick = { viewModel.onClickConfirmDeleteMood(mood = mood) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .height(48.dp),
                    shape = RoundedCornerShape(100),
                ) {
                    Text(
                        text = stringResource(R.string.yes).uppercase(),
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                TextButton(
                    onClick = viewModel::onDismissDeleteMoodDialog,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .height(48.dp),
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.onSurface
                            .copy(alpha = 0.1f),
                    )
                ) {
                    Text(
                        text = stringResource(R.string.cancel).uppercase(),
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            }
        }
    }
}