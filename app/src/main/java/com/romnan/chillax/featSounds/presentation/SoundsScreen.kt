package com.romnan.chillax.featSounds.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.romnan.chillax.core.presentation.MainViewModel
import com.romnan.chillax.core.presentation.model.SoundPresentation
import com.romnan.chillax.core.presentation.util.asString

@Composable
@Destination
@RootNavGraph(start = true) // TODO: change start destination back to MoodsScreen
fun SoundsScreen(
    viewModel: MainViewModel,
    onSoundClicked: (SoundPresentation) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column {
            Text(text = "SoundsScreen")

            viewModel.soundsList.collectAsState().value.forEach {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor =
                        if (!it.isPlaying) MaterialTheme.colors.primary
                        else MaterialTheme.colors.surface
                    ),
                    onClick = { onSoundClicked(it) }
                ) {
                    Icon(imageVector = it.icon, contentDescription = it.name.asString())
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = it.name.asString())
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = it.isPlaying.toString())
                }
            }
        }
    }
}