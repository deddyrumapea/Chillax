package com.romnan.chillax.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.romnan.chillax.domain.model.PlayerState
import com.romnan.chillax.presentation.theme.spacing
import com.romnan.chillax.presentation.util.asString

@Composable
fun PlayerSheet(
    playerState: State<PlayerState>,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn {
            val sounds = playerState.value.playingSounds
            items(sounds.size) { i ->
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(text = sounds[i].readableName.asString())
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }
        }

        Button(onClick = onStopClick) {
            Text(text = "Stop")
        }
    }
}