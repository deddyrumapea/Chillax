package com.romnan.chillax.presentation.composable.settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.PermissionChecker
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.BuildConfig
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.presentation.composable.component.DefaultDialog
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.settings.component.BasicPreference
import com.romnan.chillax.presentation.composable.settings.component.SwitchPreference
import com.romnan.chillax.presentation.composable.settings.component.ThemeChooserDialog
import com.romnan.chillax.presentation.composable.settings.component.TimePickerDialog
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.constant.IntentConstants
import com.romnan.chillax.presentation.util.asString
import com.romnan.chillax.presentation.util.handleInLaunchedEffect
import java.util.Calendar


@Composable
@Destination
fun SettingsScreen(
    viewModel: SettingsViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    val notifPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else null


    val notifPermissionReqLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionResults ->
            notifPermissions?.forEach { permission ->
                viewModel.onPermissionResults(
                    permission = permission,
                    isGranted = permissionResults[permission] == true,
                )
            }
        },
    )

    viewModel.uiEvent.handleInLaunchedEffect(snackbarHostState = snackbarHostState)

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        val themeMode = viewModel.themeMode.collectAsState()

        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
        ) {
            ScreenTitle(
                text = { stringResource(id = R.string.settings) },
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            BasicPreference(
                icon = {
                    when (themeMode.value) {
                        ThemeMode.System -> Icons.Filled.BrightnessMedium
                        ThemeMode.Light -> Icons.Filled.LightMode
                        ThemeMode.Dark -> Icons.Filled.DarkMode
                    }
                },
                title = { stringResource(R.string.pref_title_theme) },
                description = { themeMode.value.readableName.asString() },
                onClick = viewModel::showThemeChooser,
            )

            val bedtime = viewModel.bedtime.collectAsState()
            val onClickBedtimePreference: () -> Unit = {
                when {
                    notifPermissions?.any {
                        PermissionChecker.checkSelfPermission(
                            context,
                            it,
                        ) == PermissionChecker.PERMISSION_DENIED
                    } == true -> {
                        notifPermissionReqLauncher.launch(notifPermissions)
                    }

                    !bedtime.value.isActivated -> {
                        TimePickerDialog(
                            context = context,
                            initHourOfDay = bedtime.value.calendar[Calendar.HOUR_OF_DAY],
                            initMinute = bedtime.value.calendar[Calendar.MINUTE],
                            onPicked = { hourOfDay: Int, minute: Int ->
                                viewModel.onBedtimePicked(
                                    hourOfDay = hourOfDay,
                                    minute = minute,
                                )
                            },
                        ).show()
                    }

                    else -> viewModel.onTurnOffBedtime()
                }
            }
            SwitchPreference(
                icon = {
                    if (bedtime.value.isActivated) Icons.Filled.NotificationsActive
                    else Icons.Filled.Notifications
                },
                title = { stringResource(R.string.pref_title_bedtime_reminder) },
                description = { bedtime.value.readableTime?.asString() },
                checked = { bedtime.value.isActivated },
                onClick = { onClickBedtimePreference() },
                onCheckedChange = { onClickBedtimePreference() },
            )

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            BasicPreference(
                icon = { Icons.Filled.ThumbUp },
                title = { stringResource(R.string.pref_title_rate_app) },
                description = { stringResource(R.string.pref_desc_rate_app) },
                onClick = {
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(context.getString(R.string.url_app_listing))
                    }.let { context.startActivity(it) }
                },
            )

            BasicPreference(
                icon = { Icons.Filled.Share },
                title = { stringResource(R.string.pref_title_share_app) },
                description = { stringResource(R.string.pref_desc_share_app) },
                onClick = {
                    Intent(Intent.ACTION_SEND).apply {
                        type = IntentConstants.TYPE_PLAIN_TEXT
                        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text))
                    }.let { context.startActivity(it) }
                },
            )

            BasicPreference(
                icon = { Icons.Filled.Mail },
                title = { stringResource(R.string.pref_title_contact_support) },
                description = { stringResource(R.string.pref_desc_contact_support) },
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse(IntentConstants.TYPE_EMAIL)
                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            context.getString(R.string.contact_subject),
                        )
                    }
                    context.startActivity(intent)
                },
            )

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            BasicPreference(
                icon = { Icons.Filled.Help },
                title = { stringResource(R.string.pref_title_instructions) },
                onClick = { viewModel.showAppInstructions() },
            )

            BasicPreference(
                icon = { Icons.Filled.Badge },
                title = { stringResource(R.string.pref_title_attributions) },
                onClick = { viewModel.showAttributions() },
            )

            BasicPreference(
                icon = { Icons.Filled.VerifiedUser },
                title = { stringResource(R.string.pref_title_privacy_policy) },
                onClick = {
                    CustomTabsIntent.Builder()
                        .build()
                        .launchUrl(
                            context,
                            Uri.parse(context.getString(R.string.url_privacy_policy))
                        )
                },
            )

            BasicPreference(
                icon = { Icons.Filled.Code },
                title = { stringResource(R.string.source_code) },
                onClick = {
                    CustomTabsIntent.Builder()
                        .build()
                        .launchUrl(
                            context,
                            Uri.parse(context.getString(R.string.url_source_code))
                        )
                },
            )

            BasicPreference(
                icon = { Icons.Filled.Info },
                title = { stringResource(R.string.pref_title_version) },
                description = { BuildConfig.VERSION_NAME },
                onClick = viewModel::onClickAppVersion,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }

        if (viewModel.isThemeChooserVisible.collectAsState().value) ThemeChooserDialog(
            currentTheme = { themeMode.value },
            onThemeChoose = viewModel::onThemeModeChange,
            onDismissRequest = viewModel::hideThemeChooser,
        )

        if (viewModel.isAppInstructionsVisible.collectAsState().value) DefaultDialog(
            title = { stringResource(id = R.string.pref_title_instructions) },
            onDismissRequest = viewModel::hideAppInstructions
        ) {
            Text(
                text = stringResource(R.string.app_instructions),
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            )
        }

        if (viewModel.isAttributionsVisible.collectAsState().value) DefaultDialog(
            title = { stringResource(id = R.string.pref_title_attributions) },
            onDismissRequest = viewModel::hideAttributions
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = stringResource(R.string.attributions_text),
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                )
            }
        }

        viewModel.visiblePermissionDialogQueue.collectAsState().value.reversed()
            .forEach { permission ->
                DefaultDialog(
                    title = { stringResource(R.string.permission_request) },
                    onDismissRequest = viewModel::onDismissPermissionDialog,
                    contentPadding = PaddingValues(MaterialTheme.spacing.medium),
                ) {
                    Text(
                        text = if (permission == Manifest.permission.POST_NOTIFICATIONS) stringResource(
                            R.string.rationale_bed_time_reminder
                        ) else stringResource(R.string.rationale_default),
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Button(
                        onClick = {
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            ).also { context.startActivity(it) }
                        },
                        shape = RoundedCornerShape(100),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.go_to_settings),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
            }
    }
}