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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.PermissionChecker
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.chargemap.compose.numberpicker.ListItemPicker
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.presentation.composable.NavGraphs
import com.romnan.chillax.presentation.composable.appCurrentDestinationAsState
import com.romnan.chillax.presentation.composable.component.BottomBar
import com.romnan.chillax.presentation.composable.component.DefaultDialog
import com.romnan.chillax.presentation.composable.component.PlayerPeek
import com.romnan.chillax.presentation.composable.component.PlayerSheet
import com.romnan.chillax.presentation.composable.component.SaveMoodDialog
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
import com.romnan.chillax.presentation.util.handleInLaunchedEffect
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

                val player by viewModel.player.collectAsState()
                val sleepTimer by viewModel.sleepTimer.collectAsState()
                val scaffoldState = rememberScaffoldState()
                val sheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    skipHalfExpanded = true,
                )
                val saveMoodDialogState by viewModel.saveMoodDialogState.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                viewModel.uiEvent.handleInLaunchedEffect(snackbarHostState = snackbarHostState)

                ModalBottomSheetLayout(
                    sheetState = sheetState,
                    sheetContent = {
                        PlayerSheet(
                            player = { player },
                            sleepTimer = { sleepTimer },
                            onClickStop = {
                                scope.launch { sheetState.hide() }
                                viewModel.onClickStop()
                            },
                            onClickPlayPause = viewModel::onClickPlayPause,
                            onClickTimer = viewModel::onClickTimer,
                            onClickSaveMood = viewModel::onClickSaveMood,
                            onChangeSoundVolume = viewModel::onChangeSoundVolume,
                        )
                    },
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    scrimColor = Color.Black.copy(alpha = 0.5f),
                ) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        modifier = Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState,
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
                                modifier = Modifier.weight(1f),
                                dependenciesContainerBuilder = {
                                    dependency(snackbarHostState)
                                },
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
                                    SettingsScreen(
                                        viewModel = settingsViewModel,
                                        snackbarHostState = snackbarHostState,
                                    )
                                }
                            }

                            Divider(modifier = Modifier.fillMaxWidth())

                            AnimatedVisibility(visible = player.phase != PlayerPhase.STOPPED) {
                                PlayerPeek(
                                    player = { player },
                                    sleepTimer = { sleepTimer },
                                    onClickPlayPause = { viewModel.onClickPlayPause() },
                                    onClickTimer = viewModel::onClickTimer,
                                    modifier = Modifier
                                        .then(
                                            when (player.phase) {
                                                PlayerPhase.STOPPED -> Modifier
                                                else -> Modifier.clickable {
                                                    scope.launch { sheetState.show() }
                                                }
                                            },
                                        )
                                        .background(color = MaterialTheme.colors.surface)
                                        .padding(MaterialTheme.spacing.medium),
                                )
                            }
                        }

                        if (sleepTimer.isPickerDialogVisible) {
                            DefaultDialog(
                                title = { getString(R.string.set_sleep_timer) },
                                onDismissRequest = viewModel::onDismissSleepTimerPickerDialog,
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.spacing.medium),
                                ) {
                                    var pickedHours by remember { mutableStateOf(0) }
                                    var pickedMinutes by remember { mutableStateOf(30) }

                                    Text(
                                        text = getString(R.string.pause_all_sounds_after),
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                    )

                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        ListItemPicker(
                                            label = { it.toString().padStart(2, '0') },
                                            value = pickedHours,
                                            onValueChange = { value -> pickedHours = value },
                                            dividersColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                                            list = (0..12).toList(),
                                            textStyle = MaterialTheme.typography.h6,
                                        )

                                        Text(
                                            text = getString(R.string.hours),
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                        )

                                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                                        ListItemPicker(
                                            label = { it.toString().padStart(2, '0') },
                                            value = pickedMinutes,
                                            onValueChange = { value -> pickedMinutes = value },
                                            dividersColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                                            list = (0..59).step(5).toList(),
                                            textStyle = MaterialTheme.typography.h6,
                                        )

                                        Text(
                                            text = getString(R.string.minutes),
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                    Button(
                                        onClick = {
                                            viewModel.onClickSetSleepTimer(
                                                pickedHours = pickedHours,
                                                pickedMinutes = pickedMinutes,
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(100),
                                    ) {
                                        Text(
                                            text = getString(R.string.ok).uppercase(),
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }
                            }
                        }

                        if (saveMoodDialogState.showSaveMoodDialog) {
                            SaveMoodDialog(
                                state = saveMoodDialogState,
                                onDismissRequest = viewModel::onDismissSaveMoodDialog,
                                onClickConfirmSaveMood = viewModel::onClickConfirmSaveMood,
                                onPickNewMoodCustomImage = viewModel::onPickNewMoodCustomImage,
                                onClickRemoveCustomImage = viewModel::onClickRemoveCustomImage,
                            )
                        }

                        saveMoodDialogState.moodCustomImageUriToDelete?.let { uri: String ->
                            DefaultDialog(
                                title = { stringResource(R.string.remove_mood_image) },
                                onDismissRequest = viewModel::onDismissDeleteMoodImageDialog,
                            ) {
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                Text(
                                    text = stringResource(R.string.are_you_sure_you_want_to_remove_this_image),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.spacing.medium),
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                                AsyncImage(
                                    model = uri,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .size(84.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop,
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                                Button(
                                    onClick = { viewModel.onClickConfirmDeleteMoodImage(uri = uri) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.spacing.medium)
                                        .height(48.dp),
                                    shape = RoundedCornerShape(100),
                                ) {
                                    Text(
                                        text = getString(R.string.yes).uppercase(),
                                        fontWeight = FontWeight.Bold,
                                    )
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                TextButton(
                                    onClick = viewModel::onDismissDeleteMoodImageDialog,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.spacing.medium)
                                        .height(48.dp),
                                    shape = RoundedCornerShape(100),
                                    colors = ButtonDefaults.textButtonColors(
                                        backgroundColor = MaterialTheme.colors.onSurface
                                            .copy(alpha = 0.1f),
                                    ),
                                ) {
                                    Text(
                                        text = getString(R.string.cancel).uppercase(),
                                        fontWeight = FontWeight.Bold,
                                    )
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                            }
                        }
                    }
                }
            }
        }
    }
}