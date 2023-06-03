package com.romnan.chillax.presentation.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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

@Composable
fun PlayerSheet(
    player: () -> PlayerPresentation,
    sleepTimer: () -> SleepTimerPresentation,
    onStopClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onTimerClick: () -> Unit,
    onSaveMoodClick: () -> Unit,
    onSoundVolumeChange: (sound: SoundPresentation, volume: Float) -> Unit,
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
            player = player,
            sleepTimer = sleepTimer,
            onPlayPauseClick = onPlayPauseClick,
            onTimerClick = onTimerClick,
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(
                count = player().sounds.size,
                key = { i -> player().sounds[i].name },
            ) { i ->
                PlayingSoundItem(sound = { player().sounds[i] },
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
                    onVolumeChange = { onSoundVolumeChange(player().sounds[i], it) })
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
                Text(
                    text = stringResource(R.string.stop_all).uppercase(),
                    fontWeight = FontWeight.Bold,
                )
            }

//            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
//
//            Button(
//                onClick = onSaveMoodClick,
//                shape = RoundedCornerShape(percent = 100),
//                modifier = Modifier
//                    .height(48.dp)
//                    .weight(1f)
//            ) {
//                Text(text = stringResource(R.string.save_mood))
//            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    }
}