package com.romnan.chillax.core.presentation

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
import com.romnan.chillax.NavGraphs
import com.romnan.chillax.core.domain.model.PlayerPhase
import com.romnan.chillax.core.presentation.component.BottomBar
import com.romnan.chillax.core.presentation.component.PlayerBottomSheet
import com.romnan.chillax.core.presentation.service.PlayerService
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