package com.romnan.chillax.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.presentation.composable.NavGraphs
import com.romnan.chillax.presentation.composable.appCurrentDestinationAsState
import com.romnan.chillax.presentation.composable.component.BottomBar
import com.romnan.chillax.presentation.composable.component.PlayerPeek
import com.romnan.chillax.presentation.composable.component.PlayerSheet
import com.romnan.chillax.presentation.composable.destinations.MoodsScreenDestination
import com.romnan.chillax.presentation.composable.destinations.SettingsScreenDestination
import com.romnan.chillax.presentation.composable.destinations.SoundsScreenDestination
import com.romnan.chillax.presentation.composable.moods.MoodsScreen
import com.romnan.chillax.presentation.composable.moods.MoodsViewModel
import com.romnan.chillax.presentation.composable.settings.SettingsScreen
import com.romnan.chillax.presentation.composable.settings.SettingsViewModel
import com.romnan.chillax.presentation.composable.sounds.SoundsScreen
import com.romnan.chillax.presentation.composable.sounds.SoundsViewModel
import com.romnan.chillax.presentation.composable.startAppDestination
import com.romnan.chillax.presentation.composable.theme.ChillaxTheme
import com.romnan.chillax.presentation.composable.theme.spacing
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import logcat.logcat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val moodsViewModel: MoodsViewModel by viewModels()
    private val soundsViewModel: SoundsViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.themeMode.value == null }
        }

        setContent {
            val themeMode = viewModel.themeMode.collectAsState().value
            if (themeMode != null) ChillaxTheme(
                darkTheme = when (themeMode) {
                    ThemeMode.System -> isSystemInDarkTheme()
                    ThemeMode.Light -> false
                    ThemeMode.Dark -> true
                }
            ) {
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
                            player = { player },
                            onStopClick = {
                                scope.launch { sheetState.hide() }
                                viewModel.onStopClick()
                            },
                            onPlayPauseClick = viewModel::onPlayPauseClick,
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
                        bottomBar = {
                            BottomBar(
                                currentDestination = {
                                    navController.appCurrentDestinationAsState().value
                                        ?: NavGraphs.root.startAppDestination
                                },
                                onItemClick = { destination ->
                                    navController.navigate(destination.direction) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
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
                                    MoodsScreen(viewModel = moodsViewModel)
                                }

                                composable(SoundsScreenDestination) {
                                    BackHandler(enabled = sheetState.isVisible) {
                                        scope.launch { sheetState.hide() }
                                    }
                                    SoundsScreen(viewModel = soundsViewModel)
                                }

                                composable(SettingsScreenDestination) {
                                    BackHandler(enabled = sheetState.isVisible) {
                                        scope.launch { sheetState.hide() }
                                    }
                                    SettingsScreen(viewModel = settingsViewModel)
                                }
                            }

                            Divider(modifier = Modifier.fillMaxWidth())

                            AnimatedVisibility(visible = player.phase != PlayerPhase.STOPPED) {
                                PlayerPeek(
                                    player = { player },
                                    onPlayPauseClick = { viewModel.onPlayPauseClick() },
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