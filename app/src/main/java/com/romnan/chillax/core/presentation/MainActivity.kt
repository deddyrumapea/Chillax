package com.romnan.chillax.core.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.romnan.chillax.NavGraphs
import com.romnan.chillax.core.PlayerService
import com.romnan.chillax.core.presentation.component.BottomBar
import com.romnan.chillax.core.presentation.theme.ChillaxTheme
import com.romnan.chillax.destinations.MoodsScreenDestination
import com.romnan.chillax.destinations.SettingsScreenDestination
import com.romnan.chillax.destinations.SoundsScreenDestination
import com.romnan.chillax.featMoods.presentation.MoodsScreen
import com.romnan.chillax.featSettings.presentation.SettingsScreen
import com.romnan.chillax.featSounds.presentation.SoundsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

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
            viewModel.soundsList.collectLatest { soundsList ->
                val playableSounds = ArrayList(soundsList.map { it.toPlayableSound() })
                Intent(this@MainActivity, PlayerService::class.java).also {
                    it.putExtra(PlayerService.EXTRA_PLAYABLE_SOUND_ARRAYLIST, playableSounds)
                    ContextCompat.startForegroundService(this@MainActivity, it)
                }
            }
        }
    }
}