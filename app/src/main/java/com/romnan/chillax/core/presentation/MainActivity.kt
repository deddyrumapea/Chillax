package com.romnan.chillax.core.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.romnan.chillax.NavGraphs
import com.romnan.chillax.R
import com.romnan.chillax.core.domain.model.PlayerPhase
import com.romnan.chillax.core.presentation.component.BottomBar
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
                val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                    bottomSheetState = BottomSheetState(BottomSheetValue.Expanded)
                )

                Scaffold(
                    scaffoldState = scaffoldState,
                    bottomBar = { BottomBar(navController) }
                ) { scaffoldPadding ->
                    BottomSheetScaffold(
                        scaffoldState = bottomSheetScaffoldState,
                        sheetPeekHeight = 64.dp,
                        modifier = Modifier.padding(scaffoldPadding),
                        sheetContent = {
                            val playerPhase = viewModel.playerPhase.collectAsState().value
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (playerPhase != PlayerPhase.Stopped) {
                                    IconButton(onClick = { viewModel.onPlayPauseClicked() }) {
                                        Icon(
                                            imageVector = if (playerPhase == PlayerPhase.Playing)
                                                Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                            contentDescription = stringResource(
                                                if (playerPhase == PlayerPhase.Playing)
                                                    R.string.pause else R.string.play
                                            )
                                        )
                                    }

                                    IconButton(onClick = { viewModel.onStopClicked() }) {
                                        Icon(
                                            imageVector = Icons.Default.StopCircle,
                                            contentDescription = stringResource(R.string.stop)
                                        )
                                    }
                                }
                            }
                        }) { bottomSheetScaffoldPadding ->

                        val scrollState = rememberScrollState()
                        DestinationsNavHost(
                            engine = engine,
                            navController = navController,
                            navGraph = NavGraphs.root,
                            modifier = Modifier
                                .padding(bottomSheetScaffoldPadding)
                                .verticalScroll(scrollState)
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