package com.romnan.chillax.featSounds.presentation

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.romnan.chillax.R
import logcat.logcat

@Composable
@Destination
@RootNavGraph(start = true) // TODO: change start destination back to MoodsScreen
fun SoundsScreen() {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column {
            Text(text = "SoundsScreen")

            Button(
                onClick = {
                    playWithExoplayer(context)
                    logcat("SoundScreen") { "Rain Clicked" }
                }
            ) {
                Text(text = "Exoplayer")
            }
        }
    }
}

@OptIn(UnstableApi::class)
fun playWithExoplayer(context: Context) {
    ExoPlayer.Builder(context)
        .apply {
            setAudioAttributes(AudioAttributes.DEFAULT, true)
            setHandleAudioBecomingNoisy(true)
        }
        .build().apply {
            val uri = RawResourceDataSource.buildRawResourceUri(R.raw.sound_rain)
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            prepare()
            playWhenReady = true
        }
}