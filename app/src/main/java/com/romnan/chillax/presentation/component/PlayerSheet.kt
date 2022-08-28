package com.romnan.chillax.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.PlayerState
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.presentation.theme.spacing

@Composable
fun PlayerSheet(
    playerState: PlayerState,
    onStopClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onTimerClick: () -> Unit,
    onSaveMoodClick: () -> Unit,
    onSoundVolumeChange:(sound: Sound, volumeLevel: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = MaterialTheme.spacing.medium),
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        BottomSheetHandleBar(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PlayerPeek(
            playerState = playerState,
            onPlayPauseClick = onPlayPauseClick,
            onTimerClick = onTimerClick,
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(playerState.playingSounds.size) { i ->
                PlayingSoundItem(
                    sound = playerState.playingSounds[i],
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
                    onVolumeChange = { onSoundVolumeChange(playerState.playingSounds[i], it) }
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onStopClick,
                shape = RoundedCornerShape(percent = 100),
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
            ) {
                Text(text = stringResource(R.string.stop_all))
            }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

            Button(
                onClick = onSaveMoodClick,
                shape = RoundedCornerShape(percent = 100),
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
            ) {
                Text(text = stringResource(R.string.save_mood))
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    }
}