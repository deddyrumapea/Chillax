package com.romnan.chillax.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.presentation.component.BottomBar
import com.romnan.chillax.presentation.component.PlayerBottomSheet
import com.romnan.chillax.data.service.PlayerService
import com.romnan.chillax.presentation.theme.ChillaxTheme
import com.romnan.chillax.presentation.destinations.MoodsScreenDestination
import com.romnan.chillax.presentation.destinations.SettingsScreenDestination
import com.romnan.chillax.presentation.destinations.SoundsScreenDestination
import com.romnan.chillax.presentation.moods.MoodsScreen
import com.romnan.chillax.presentation.settings.SettingsScreen
import com.romnan.chillax.presentation.sounds.SoundsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.playerPhase.collectLatest { phase ->
                // TODO: put this inside a use case
                Intent(this@MainActivity, PlayerService::class.java).also { intent ->

                    when (phase) {
                        PlayerPhase.Playing -> ContextCompat
                            .startForegroundService(this@MainActivity, intent)

                        PlayerPhase.Paused -> ContextCompat
                            .startForegroundService(this@MainActivity, intent)

                        PlayerPhase.Stopped -> stopService(intent)
                    }
                }
            }
        }

        setContent {
            ChillaxTheme {
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()

                val scaffoldState = rememberScaffoldState()
                val bsState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
                val bsScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bsState)

                LaunchedEffect(key1 = 1) {
                    viewModel.playerPhase.collectLatest {
                        if (it == PlayerPhase.Stopped) {
                            bsScaffoldState.bottomSheetState.collapse()
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    bottomBar = { BottomBar(navController) }
                ) { scaffoldPadding ->

                    val peekHeight =
                        if (viewModel.playerPhase.collectAsState().value != PlayerPhase.Stopped)
                            70.dp else 0.dp

                    BottomSheetScaffold(
                        scaffoldState = bsScaffoldState,
                        sheetPeekHeight = peekHeight,
                        modifier = Modifier
                            .padding(scaffoldPadding)
                            .fillMaxSize(),
                        sheetContent = {
                            PlayerBottomSheet(
                                playerPhase = viewModel.playerPhase.collectAsState().value,
                                bottomSheetState = bsScaffoldState.bottomSheetState,
                                peekHeight = peekHeight,
                                onPlayPauseClicked = viewModel::onPlayPauseClicked,
                                onStopClicked = viewModel::onStopClicked
                            )
                        }) { bsScaffoldPadding ->

                        // TODO: fix scrolling behavior.
                        DestinationsNavHost(
                            engine = engine,
                            navController = navController,
                            navGraph = NavGraphs.root,
                            modifier = Modifier.padding(bsScaffoldPadding)
                        ) {
                            composable(MoodsScreenDestination) {
                                MoodsScreen(viewModel = viewModel)
                            }

                            composable(SoundsScreenDestination) {
                                SoundsScreen(viewModel = viewModel)
                            }

                            composable(SettingsScreenDestination) {
                                SettingsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}