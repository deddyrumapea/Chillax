package com.romnan.chillax.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.PermissionChecker
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.chargemap.compose.numberpicker.ListItemPicker
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.presentation.composable.NavGraphs
import com.romnan.chillax.presentation.composable.appCurrentDestinationAsState
import com.romnan.chillax.presentation.composable.component.BottomBar
import com.romnan.chillax.presentation.composable.component.PlayerPeek
import com.romnan.chillax.presentation.composable.component.PlayerSheet
import com.romnan.chillax.presentation.composable.component.SaveMixDialog
import com.romnan.chillax.presentation.composable.destinations.MixesScreenDestination
import com.romnan.chillax.presentation.composable.destinations.SettingsScreenDestination
import com.romnan.chillax.presentation.composable.destinations.SoundsScreenDestination
import com.romnan.chillax.presentation.composable.mixes.MixesScreen
import com.romnan.chillax.presentation.composable.mixes.MixesViewModel
import com.romnan.chillax.presentation.composable.settings.SettingsScreen
import com.romnan.chillax.presentation.composable.settings.SettingsViewModel
import com.romnan.chillax.presentation.composable.sounds.SoundsScreen
import com.romnan.chillax.presentation.composable.sounds.SoundsViewModel
import com.romnan.chillax.presentation.composable.startAppDestination
import com.romnan.chillax.presentation.composable.theme.AppTheme
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.util.handleInLaunchedEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import logcat.logcat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val mixesViewModel: MixesViewModel by viewModels()
    private val soundsViewModel: SoundsViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.themeMode.value == null }
        }

        if ((PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PermissionChecker.PERMISSION_DENIED) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) {
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
            ) { permissionRequestResults ->
                logcat { permissionRequestResults.toString() }
            }.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }

        setContent {
            val themeMode = viewModel.themeMode.collectAsState().value
            if (themeMode != null) AppTheme(
                darkTheme = when (themeMode) {
                    ThemeMode.System -> isSystemInDarkTheme()
                    ThemeMode.Light -> false
                    ThemeMode.Dark -> true
                }
            ) {
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()

                val scope = rememberCoroutineScope()

                val player by viewModel.player.collectAsState()
                val sleepTimer by viewModel.sleepTimer.collectAsState()
                val sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                )
                val saveMixDialogState by viewModel.saveMixDialogState.collectAsState()

                viewModel.uiEvent.handleInLaunchedEffect()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(
                            currentDestination = {
                                navController.appCurrentDestinationAsState().value
                                    ?: NavGraphs.root.startAppDestination
                            },
                            onClickItem = { destination ->
                                navController.navigate(destination.direction) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    },
                ) { scaffoldPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(scaffoldPadding),
                    ) {
                        DestinationsNavHost(
                            engine = engine,
                            navController = navController,
                            navGraph = NavGraphs.root,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        ) {
                            composable(MixesScreenDestination) {
                                BackHandler(enabled = sheetState.isVisible) {
                                    scope.launch { sheetState.hide() }
                                }
                                MixesScreen(viewModel = mixesViewModel)
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
                                SettingsScreen(
                                    viewModel = settingsViewModel,
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.fillMaxWidth())

                        AnimatedVisibility(visible = player.phase != PlayerPhase.STOPPED) {
                            val containerColor = NavigationBarDefaults.containerColor
                            val contentColor =
                                MaterialTheme.colorScheme.contentColorFor(containerColor)

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = containerColor,
                                contentColor = contentColor,
                                tonalElevation = NavigationBarDefaults.Elevation,
                                onClick = { scope.launch { sheetState.show() } },
                                enabled = player.phase != PlayerPhase.STOPPED,
                            ) {
                                PlayerPeek(
                                    player = { player },
                                    sleepTimer = { sleepTimer },
                                    onClickPlayPause = { viewModel.onClickPlayPause() },
                                    onClickTimer = viewModel::onClickTimer,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.spacing.medium),
                                )
                            }
                        }
                    }

                    if (sheetState.isVisible) {
                        ModalBottomSheet(
                            onDismissRequest = { scope.launch { sheetState.hide() } },
                            sheetState = sheetState,
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = NavigationBarDefaults.containerColor,
                            contentColor = MaterialTheme.colorScheme.contentColorFor(
                                NavigationBarDefaults.containerColor,
                            ),
                            tonalElevation = NavigationBarDefaults.Elevation,
                        ) {
                            PlayerSheet(
                                player = { player },
                                sleepTimer = { sleepTimer },
                                onClickStop = {
                                    scope.launch { sheetState.hide() }
                                    viewModel.onClickStop()
                                },
                                onClickPlayPause = viewModel::onClickPlayPause,
                                onClickTimer = viewModel::onClickTimer,
                                onClickSaveMix = viewModel::onClickSaveMix,
                                onChangeSoundVolume = viewModel::onChangeSoundVolume,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    if (sleepTimer.isPickerDialogVisible) {
                        var pickedHours by remember { mutableIntStateOf(0) }
                        var pickedMinutes by remember { mutableIntStateOf(30) }

                        AlertDialog(
                            onDismissRequest = viewModel::onDismissSleepTimerPickerDialog,
                            title = {
                                Text(text = getString(R.string.pause_all_sounds_after))
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        viewModel.onClickSetSleepTimer(
                                            pickedHours = pickedHours,
                                            pickedMinutes = pickedMinutes,
                                        )
                                    },
                                    shape = RoundedCornerShape(100),
                                ) {
                                    Text(text = stringResource(R.string.set_timer))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = viewModel::onDismissSleepTimerPickerDialog) {
                                    Text(text = stringResource(R.string.cancel))
                                }
                            },
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    ListItemPicker(
                                        label = { it.toString().padStart(2, '0') },
                                        value = pickedHours,
                                        onValueChange = { value -> pickedHours = value },
                                        dividersColor = MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.5f
                                        ),
                                        list = (0..12).toList(),
                                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurface,
                                        ),
                                    )

                                    Text(text = getString(R.string.hours))

                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                                    ListItemPicker(
                                        label = { it.toString().padStart(2, '0') },
                                        value = pickedMinutes,
                                        onValueChange = { value -> pickedMinutes = value },
                                        dividersColor = MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.5f
                                        ),
                                        list = (0..59).step(5).toList(),
                                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurface,
                                        ),
                                    )

                                    Text(
                                        text = getString(R.string.minutes),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    )
                                }
                            }
                        )
                    }

                    if (saveMixDialogState.showSaveMixDialog) {
                        SaveMixDialog(
                            state = saveMixDialogState,
                            onDismissRequest = viewModel::onDismissSaveMixDialog,
                            onClickConfirmSaveMix = viewModel::onClickConfirmSaveMix,
                            onPickNewMixCustomImage = viewModel::onPickNewMixCustomImage,
                            onClickRemoveCustomImage = viewModel::onClickRemoveCustomImage,
                        )
                    }

                    saveMixDialogState.mixCustomImageUriToDelete?.let { uri: String ->
                        AlertDialog(
                            onDismissRequest = viewModel::onDismissDeleteMixImageDialog,
                            title = { Text(text = stringResource(R.string.are_you_sure_you_want_to_remove_this_image)) },
                            text = {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(84.dp)
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = { viewModel.onClickConfirmDeleteMixImage(uri = uri) },
                                    shape = RoundedCornerShape(100),
                                ) {
                                    Text(text = stringResource(R.string.remove_image))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = viewModel::onDismissDeleteMixImageDialog) {
                                    Text(text = stringResource(R.string.cancel))
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}