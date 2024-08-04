package com.romnan.chillax.presentation.composable.moods

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.presentation.composable.component.DefaultDialog
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.moods.component.MoodItem
import com.romnan.chillax.presentation.composable.theme.Blue400
import com.romnan.chillax.presentation.composable.theme.Blue700
import com.romnan.chillax.presentation.composable.theme.DarkGreen400
import com.romnan.chillax.presentation.composable.theme.Gold700
import com.romnan.chillax.presentation.composable.theme.LightBlue400
import com.romnan.chillax.presentation.composable.theme.Orange900
import com.romnan.chillax.presentation.composable.theme.Pink400
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.util.asString

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
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
                        start = MaterialTheme.spacing.small,
                        top = MaterialTheme.spacing.large,
                        end = MaterialTheme.spacing.small,
                        bottom = MaterialTheme.spacing.medium,
                    ),
                )
            }

            items(
                items = state.moods,
                key = { mood: Mood -> mood.id },
            ) { mood: Mood ->
                Box(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(MaterialTheme.spacing.extraSmall)
                        .fillMaxWidth(),
                ) {
                    MoodItem(
                        mood = { mood },
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.extraSmall)
                            .fillMaxSize()
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(16.dp),
                                clip = true,
                            )
                            .then(
                                when (state.player?.playingMood?.id == mood.id) {
                                    true -> Modifier
                                        .border(
                                            width = 3.dp,
                                            brush = Brush.sweepGradient(
                                                colors = listOf(
                                                    DarkGreen400,
                                                    Gold700,
                                                    Orange900,
                                                    LightBlue400,
                                                    Blue700,
                                                    Blue400,
                                                ),
                                            ),
                                            shape = RoundedCornerShape(16.dp),
                                        )

                                    false -> Modifier
                                }
                            )
                            .then(
                                when (mood.isCustom) {
                                    true -> Modifier.combinedClickable(
                                        onClick = { viewModel.onClickMood(mood = mood) },
                                        onLongClick = { viewModel.onLongClickMood(mood = mood) }
                                    )

                                    false -> Modifier.clickable(
                                        onClick = { viewModel.onClickMood(mood = mood) },
                                    )
                                }
                            ),
                    )

                    if (state.customMoodIdToShowDeleteButton == mood.id) {
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentEnforcement provides false
                        ) {
                            IconButton(
                                onClick = { viewModel.onClickDeleteMood(mood = mood) },
                                modifier = Modifier.align(Alignment.TopEnd),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircle,
                                    contentDescription = null,
                                    tint = Pink400,
                                    modifier = Modifier.background(
                                        color = Color.White,
                                        shape = CircleShape,
                                    ),
                                )
                            }
                        }
                    }
                }
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

                MoodItem(
                    mood = { mood },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(144.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(16.dp),
                            clip = true,
                        ),
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