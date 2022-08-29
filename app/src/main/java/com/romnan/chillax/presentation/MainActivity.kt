package com.romnan.chillax.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.romnan.chillax.data.service.PlayerService
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.presentation.component.BottomBar
import com.romnan.chillax.presentation.component.PlayerPeek
import com.romnan.chillax.presentation.component.PlayerSheet
import com.romnan.chillax.presentation.destinations.MoodsScreenDestination
import com.romnan.chillax.presentation.destinations.SettingsScreenDestination
import com.romnan.chillax.presentation.destinations.SoundsScreenDestination
import com.romnan.chillax.presentation.moods.MoodsScreen
import com.romnan.chillax.presentation.settings.SettingsScreen
import com.romnan.chillax.presentation.sounds.SoundsScreen
import com.romnan.chillax.presentation.theme.ChillaxTheme
import com.romnan.chillax.presentation.theme.spacing
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.player.collectLatest {
                // TODO: put this inside a use case
                Intent(this@MainActivity, PlayerService::class.java).also { intent ->

                    when (it.phase) {
                        PlayerPhase.PLAYING -> ContextCompat
                            .startForegroundService(this@MainActivity, intent)

                        PlayerPhase.PAUSED -> ContextCompat
                            .startForegroundService(this@MainActivity, intent)

                        PlayerPhase.STOPPED -> stopService(intent)
                    }
                }
            }
        }

        setContent {
            ChillaxTheme(darkTheme = true) {
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()

                val scope = rememberCoroutineScope()

                val scaffoldState = rememberScaffoldState()
                val player = viewModel.player.collectAsState().value
                val sheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    skipHalfExpanded = true,
                )

                ModalBottomSheetLayout(
                    sheetState = sheetState,
                    sheetContent = {
                        PlayerSheet(
                            player = player,
                            onStopClick = {
                                scope.launch { sheetState.hide() }
                                viewModel.onStopClicked()
                            },
                            onPlayPauseClick = viewModel::onPlayPauseClicked,
                            onTimerClick = { logcat { "onTimerClick()" } /* TODO */ },
                            onSaveMoodClick = { logcat { "onSaveMoodClick()" } /* TODO */ },
                            onSoundVolumeChange = viewModel::onSoundVolumeChange,
                        )
                    },
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    scrimColor = Color.Black.copy(alpha = 0.5f),
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState,
                        bottomBar = { BottomBar(navController) }
                    ) { scaffoldPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(scaffoldPadding)
                        ) {
                            DestinationsNavHost(
                                engine = engine,
                                navController = navController,
                                navGraph = NavGraphs.root,
                                modifier = Modifier.weight(1f)
                            ) {
                                composable(MoodsScreenDestination) {
                                    BackHandler(enabled = sheetState.isVisible) {
                                        scope.launch { sheetState.hide() }
                                    }
                                    MoodsScreen(viewModel = viewModel)
                                }

                                composable(SoundsScreenDestination) {
                                    BackHandler(enabled = sheetState.isVisible) {
                                        scope.launch { sheetState.hide() }
                                    }
                                    SoundsScreen(viewModel = viewModel)
                                }

                                composable(SettingsScreenDestination) {
                                    BackHandler(enabled = sheetState.isVisible) {
                                        scope.launch { sheetState.hide() }
                                    }
                                    SettingsScreen()
                                }
                            }

                            AnimatedVisibility(visible = player.phase != PlayerPhase.STOPPED) {
                                PlayerPeek(
                                    player = player,
                                    onPlayPauseClick = viewModel::onPlayPauseClicked,
                                    onTimerClick = { logcat { "onTimerClick()" } },
                                    modifier = Modifier
                                        .clickable { scope.launch { sheetState.show() } }
                                        .background(color = MaterialTheme.colors.surface)
                                        .padding(MaterialTheme.spacing.medium),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}