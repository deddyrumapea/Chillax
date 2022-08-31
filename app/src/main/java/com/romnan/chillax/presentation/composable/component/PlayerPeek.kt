package com.romnan.chillax.presentation.composable.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.model.PlayerPresentation
import com.romnan.chillax.presentation.util.asString

@Composable
fun PlayerPeek(
    player: PlayerPresentation,
    onPlayPauseClick: () -> Unit,
    onTimerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(text = player.soundsTitle.asString())
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(text = stringResource(R.string.timer_disabled))
            }
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        IconButton(onClick = onTimerClick) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = stringResource(R.string.set_timer),
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        Button(
            onClick = onPlayPauseClick,
            shape = CircleShape,
        ) {
            Icon(
                imageVector =
                if (player.phase == PlayerPhase.PLAYING) Icons.Default.Pause
                else Icons.Default.PlayArrow,
                contentDescription = stringResource(
                    if (player.phase == PlayerPhase.PLAYING) R.string.pause
                    else R.string.play
                ),
            )
        }
    }
}