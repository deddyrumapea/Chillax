package com.romnan.chillax.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.romnan.chillax.NavGraphs
import com.romnan.chillax.core.domain.model.Sound
import com.romnan.chillax.core.presentation.component.BottomBar
import com.romnan.chillax.core.presentation.theme.ChillaxTheme
import com.romnan.chillax.core.presentation.util.asString
import com.romnan.chillax.destinations.MoodsScreenDestination
import com.romnan.chillax.destinations.SettingsScreenDestination
import com.romnan.chillax.destinations.SoundsScreenDestination
import com.romnan.chillax.featMoods.presentation.MoodsScreen
import com.romnan.chillax.featSettings.presentation.SettingsScreen
import com.romnan.chillax.featSounds.presentation.SoundsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import logcat.logcat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChillaxTheme {
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    scaffoldState = scaffoldState,
                    bottomBar = { BottomBar(navController) }
                ) {
                    DestinationsNavHost(
                        engine = engine,
                        navController = navController,
                        navGraph = NavGraphs.root
                    ) {
                        composable(MoodsScreenDestination) { MoodsScreen() }

                        composable(SoundsScreenDestination) {
                            SoundsScreen(
                                viewModel = viewModel,
                                onSoundClicked = viewModel::onSoundClicked
                            )
                        }

                        composable(SettingsScreenDestination) { SettingsScreen() }
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.playableSoundsList.collectLatest { soundsList ->
                val playing = soundsList.filter { it.isPlaying }
                val notPlaying = soundsList.filter { !it.isPlaying }

                playing.forEach { playSound(it.toSound()) }
                notPlaying.forEach { stopPlayingSound(it.toSound()) }
            }
        }
    }

    private val soundPlayers = mutableMapOf<Sound, ExoPlayer>()

    private fun playSound(sound: Sound) {
        if (soundPlayers[sound] == null) { // The sound has not been played yet
            val player = ExoPlayer.Builder(this)
                .apply { setHandleAudioBecomingNoisy(true) }
                .build()
                .apply {
                    val uri = RawResourceDataSource.buildRawResourceUri(sound.resource)
                    setMediaItem(MediaItem.fromUri(uri))
                    repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    prepare()
                    play()
                }
            soundPlayers[sound] = player
            logcat { "PLAY ${sound.name.asString(this)}" }
        }
    }

    private fun stopPlayingSound(sound: Sound) {
        soundPlayers[sound]?.let {
            if (it.isPlaying) { // The sound is currently playing
                it.playWhenReady = false
                it.stop()
                it.release()
                soundPlayers.remove(sound)
                logcat { "STOP ${sound.name.asString(this)}" }
            }
        }
    }
}