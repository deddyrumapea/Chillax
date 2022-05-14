package com.romnan.chillax.core.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: start intent when the player state is playing
        Intent(this@MainActivity, PlayerService::class.java).also { intent ->
            ContextCompat.startForegroundService(this@MainActivity, intent)
        }

        setContent {
            ChillaxTheme {
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        Row {
                            Button(onClick = { viewModel.onPlayPauseClicked() }) {
                                Text(text = if (viewModel.isPlaying.collectAsState().value) "Pause" else "Play")
                            }

                            Button(onClick = { viewModel.onStopClicked() }) {
                                Text(text = "Stop")
                            }
                        }
                    },
                    bottomBar = { BottomBar(navController) }
                ) {
                    DestinationsNavHost(
                        engine = engine,
                        navController = navController,
                        navGraph = NavGraphs.root
                    ) {
                        composable(MoodsScreenDestination) {
                            MoodsScreen(viewModel = viewModel)
                        }

                        composable(SoundsScreenDestination) {
                            SoundsScreen(viewModel = viewModel)
                        }

                        composable(SettingsScreenDestination) { SettingsScreen() }
                    }
                }
            }
        }
    }
}