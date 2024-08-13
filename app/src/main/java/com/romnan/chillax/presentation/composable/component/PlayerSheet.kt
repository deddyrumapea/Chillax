package com.romnan.chillax.presentation.composable.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romnan.chillax.R
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.model.PlayerPresentation
import com.romnan.chillax.presentation.model.SleepTimerPresentation
import com.romnan.chillax.presentation.model.SoundPresentation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerSheet(
    player: () -> PlayerPresentation,
    sleepTimer: () -> SleepTimerPresentation,
    onClickStop: () -> Unit,
    onClickPlayPause: () -> Unit,
    onClickTimer: () -> Unit,
    onClickSaveMood: () -> Unit,
    onChangeSoundVolume: (
        soundId: String,
        newVolume: Float,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = MaterialTheme.spacing.medium),
    ) {
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            BottomSheetHandleBar(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            PlayerPeek(
                player = player,
                sleepTimer = sleepTimer,
                onClickPlayPause = onClickPlayPause,
                onClickTimer = onClickTimer,
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.medium)
            )
        }

        items(
            items = player().playingSounds,
            key = { sound: SoundPresentation -> sound.id },
        ) { sound: SoundPresentation ->
            PlayingSoundItem(
                sound = { sound },
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(vertical = MaterialTheme.spacing.small),
                onVolumeChange = { newVolume: Float ->
                    onChangeSoundVolume(sound.id, newVolume)
                },
            )
        }

        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.medium)
            )

            AnimatedVisibility(visible = player().playingMood == null) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = onClickSaveMood,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(100),
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.onSurface
                                .copy(alpha = 0.1f),
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.save_mood).uppercase(),
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
            }

            Button(
                onClick = onClickStop,
                shape = RoundedCornerShape(percent = 100),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(
                    text = stringResource(R.string.stop_all).uppercase(),
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}