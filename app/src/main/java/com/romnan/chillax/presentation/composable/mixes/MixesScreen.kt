package com.romnan.chillax.presentation.composable.mixes

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
import com.romnan.chillax.domain.model.Mix
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.mixes.component.MixItem
import com.romnan.chillax.presentation.composable.mixes.model.MixType
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.util.asString

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Destination
@RootNavGraph(start = true)
fun MixesScreen(
    viewModel: MixesViewModel
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
                text = { stringResource(id = R.string.mixes) },
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
                    items = state.mixTypes,
                ) { mixType: MixType ->
                    val mixTypeBgColor by animateColorAsState(
                        targetValue = when (mixType == state.selectedMixType) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> MaterialTheme.colorScheme.surface
                        },
                    )

                    val mixTypeContentColor by animateColorAsState(
                        targetValue = when (mixType == state.selectedMixType) {
                            true -> MaterialTheme.colorScheme.onPrimary
                            false -> MaterialTheme.colorScheme.onSurface
                        },
                    )

                    Surface(
                        onClick = { viewModel.onSelectMixType(mixType) },
                        color = mixTypeBgColor,
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
                                imageVector = mixType.icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = mixTypeContentColor,
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                            Text(
                                text = mixType.label.asString(),
                                fontWeight = FontWeight.SemiBold,
                                color = mixTypeContentColor,
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
                visible = state.mixes.isEmpty() && state.selectedMixType == MixType.Custom,
            ) {
                Text(
                    text = stringResource(R.string.it_s_empty_when_you_save_mixes_they_will_show_up_here),
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
            items = state.mixes,
            key = { mix: Mix -> mix.id },
        ) { mix: Mix ->
            MixItem(
                mix = { mix },
                isPlaying = state.player?.phase == PlayerPhase.PLAYING && state.player?.playingMix?.id == mix.id,
                onClickPlayOrPause = viewModel::onClickPlayOrPause,
                onClickDelete = viewModel::onClickDeleteMix,
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

    state.customMixToDelete?.let { mix: Mix ->
        AlertDialog(
            title = {
                Text(
                    text = stringResource(
                        R.string.are_you_sure_you_want_to_delete_x,
                        mix.readableName.asString(),
                    ),
                )
            },
            onDismissRequest = viewModel::onDismissDeleteMixDialog,
            confirmButton = {
                Button(
                    onClick = { viewModel.onClickConfirmDeleteMix(mix = mix) },
                ) {
                    Text(
                        text = stringResource(
                            R.string.delete_x,
                            mix.readableName.asString(),
                        ),
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = viewModel::onDismissDeleteMixDialog,
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            text = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = mix.imageUri,
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