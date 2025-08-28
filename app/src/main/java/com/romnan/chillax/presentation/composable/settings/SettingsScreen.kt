package com.romnan.chillax.presentation.composable.settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.BuildConfig
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.settings.component.BasicPreference
import com.romnan.chillax.presentation.composable.settings.component.SwitchPreference
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
) {
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

    viewModel.uiEvent.handleInLaunchedEffect()

    val themeMode = viewModel.themeMode.collectAsState()

    Column(
        modifier = Modifier
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
                    data = context.getString(R.string.url_app_listing).toUri()
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
                    data = IntentConstants.TYPE_EMAIL.toUri()
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
            icon = { Icons.AutoMirrored.Filled.Help },
            title = { stringResource(R.string.pref_title_instructions) },
            onClick = { viewModel.showAppInstructions() },
        )

        BasicPreference(
            icon = { Icons.Filled.WavingHand },
            title = { stringResource(R.string.pref_title_attributions) },
            onClick = viewModel::showAttributions,
        )

        BasicPreference(
            icon = { Icons.Filled.Info },
            title = { stringResource(R.string.pref_title_version) },
            description = { BuildConfig.VERSION_NAME },
            onClick = viewModel::onClickAppVersion,
        )

        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

        BasicPreference(
            icon = { Icons.AutoMirrored.Filled.OpenInNew },
            title = { stringResource(R.string.other_apps) },
            onClick = {
                Intent(Intent.ACTION_VIEW).apply {
                    data = context.getString(R.string.url_developer_page).toUri()
                }.let { context.startActivity(it) }
            },
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    }

    val isThemeChooserVisible by viewModel.isThemeChooserVisible.collectAsState()
    if (isThemeChooserVisible) {
        AlertDialog(
            onDismissRequest = viewModel::hideThemeChooser,
            title = { Text(text = stringResource(R.string.choose_theme)) },
            confirmButton = {
                Button(
                    onClick = viewModel::hideThemeChooser,
                    shape = RoundedCornerShape(100),
                ) {
                    Text(text = stringResource(R.string.done))
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
                    ThemeMode.entries.forEach { mode: ThemeMode ->
                        FilledTonalIconToggleButton(
                            checked = mode == themeMode.value,
                            onCheckedChange = { viewModel.onThemeModeChange(mode) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(text = mode.readableName.asString())
                        }
                    }
                }
            },
        )
    }

    val isAppInstructionsVisible by viewModel.isAppInstructionsVisible.collectAsState()
    if (isAppInstructionsVisible) {
        AlertDialog(
            onDismissRequest = viewModel::hideAppInstructions,
            title = { Text(text = stringResource(id = R.string.pref_title_instructions)) },
            text = { Text(text = stringResource(R.string.app_instructions)) },
            confirmButton = {
                Button(
                    onClick = viewModel::hideAppInstructions,
                    shape = RoundedCornerShape(100),
                ) {
                    Text(text = stringResource(R.string.close))
                }
            },
        )
    }

    val isAttributionsVisible by viewModel.isAttributionsVisible.collectAsState()
    if (isAttributionsVisible) {
        AlertDialog(
            onDismissRequest = viewModel::hideAttributions,
            title = { Text(text = stringResource(id = R.string.pref_title_attributions)) },
            text = {
                Text(
                    text = attributionsAnnotatedString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                )
            },
            confirmButton = {
                Button(
                    onClick = viewModel::hideAttributions,
                    shape = RoundedCornerShape(100),
                ) {
                    Text(text = stringResource(R.string.close))
                }
            },
        )
    }

    val visiblePermissionDialogQueue by viewModel.visiblePermissionDialogQueue.collectAsState()
    visiblePermissionDialogQueue.reversed().forEach { permission ->
        AlertDialog(
            title = { Text(stringResource(R.string.grant_permission)) },
            onDismissRequest = viewModel::onDismissPermissionDialog,
            confirmButton = {
                Button(
                    onClick = {
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        ).also { context.startActivity(it) }
                    },
                    shape = RoundedCornerShape(100),
                ) {
                    Text(text = stringResource(R.string.go_to_settings))
                }
            },
            text = {
                Text(
                    text = when (permission) {
                        Manifest.permission.POST_NOTIFICATIONS -> stringResource(R.string.rationale_bed_time_reminder)
                        else -> stringResource(R.string.rationale_default)
                    },
                )
            },
        )
    }
}