package com.romnan.chillax.presentation.composable.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.BuildConfig
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.settings.component.*
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.constant.IntentConstants
import com.romnan.chillax.presentation.util.asString
import logcat.logcat
import java.util.*

@Composable
@Destination
fun SettingsScreen(
    viewModel: SettingsViewModel,
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        val themeMode = viewModel.themeMode.collectAsState()
        val bedtime = viewModel.bedtime.collectAsState()

        LazyColumn(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            item { ScreenTitle(text = { stringResource(id = R.string.settings) }) }

            item {
                BasicPreference(
                    icon = {
                        when (themeMode.value) {
                            ThemeMode.System -> Icons.Default.BrightnessMedium
                            ThemeMode.Light -> Icons.Default.LightMode
                            ThemeMode.Dark -> Icons.Default.DarkMode
                        }
                    },
                    title = { stringResource(R.string.pref_title_theme) },
                    description = { themeMode.value.readableName.asString() },
                    onClick = viewModel::showThemeChooser,
                )
            }

            item {
                SwitchPreference(
                    icon = {
                        if (bedtime.value.isActivated) Icons.Default.NotificationsActive
                        else Icons.Default.Notifications
                    },
                    title = { stringResource(R.string.pref_title_bedtime_reminder) },
                    description = { bedtime.value.readableTime?.asString() },
                    checked = { bedtime.value.isActivated },
                    onClick = {
                        if (!bedtime.value.isActivated) TimePickerDialog(
                            context = context,
                            initHourOfDay = bedtime.value.calendar[Calendar.HOUR_OF_DAY],
                            initMinute = bedtime.value.calendar[Calendar.MINUTE],
                            onPicked = { hourOfDay: Int, minute: Int ->
                                viewModel.onBedtimePicked(hourOfDay = hourOfDay, minute = minute)
                            },
                        ).show() else viewModel.onTurnOffBedtime()
                    },
                    onCheckedChange = {
                        if (!bedtime.value.isActivated) TimePickerDialog(
                            context = context,
                            initHourOfDay = bedtime.value.calendar[Calendar.HOUR_OF_DAY],
                            initMinute = bedtime.value.calendar[Calendar.MINUTE],
                            onPicked = { hourOfDay: Int, minute: Int ->
                                viewModel.onBedtimePicked(hourOfDay = hourOfDay, minute = minute)
                            },
                        ).show() else viewModel.onTurnOffBedtime()
                    }
                )
            }

            item { Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small)) }

            item {
                BasicPreference(
                    icon = { Icons.Default.RateReview },
                    title = { stringResource(R.string.pref_title_rate_app) },
                    description = { stringResource(R.string.pref_desc_rate_app) },
                    onClick = {
                        Intent(Intent.ACTION_VIEW)
                            .apply { data = Uri.parse(context.getString(R.string.url_app_listing)) }
                            .let { context.startActivity(it) }
                    },
                )
            }

            item {
                BasicPreference(
                    icon = { Icons.Default.Share },
                    title = { stringResource(R.string.pref_title_share_app) },
                    description = { stringResource(R.string.pref_desc_share_app) },
                    onClick = {
                        Intent(Intent.ACTION_SEND).apply {
                            type = IntentConstants.TYPE_PLAIN_TEXT
                            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text))
                        }.let { context.startActivity(it) }
                    },
                )
            }

            item {
                BasicPreference(
                    icon = { Icons.Default.ContactMail },
                    title = { stringResource(R.string.pref_title_contact_support) },
                    description = { stringResource(R.string.pref_desc_contact_support) },
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse(IntentConstants.TYPE_EMAIL)
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                context.getString(R.string.contact_subject)
                            )
                        }
                        context.startActivity(intent)
                    },
                )
            }

            item { Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small)) }

            item {
                BasicPreference(
                    icon = { Icons.Default.IntegrationInstructions },
                    title = { stringResource(R.string.pref_title_app_instructions) },
                    onClick = { viewModel.showAppInstructions() },
                )
            }

            item {
                BasicPreference(
                    icon = { Icons.Default.Attribution },
                    title = { stringResource(R.string.pref_title_attributions) },
                    onClick = { viewModel.showAttributions() },
                )
            }

            item {
                BasicPreference(
                    icon = { Icons.Default.PrivacyTip },
                    title = { stringResource(R.string.pref_title_privacy_policy) },
                    onClick = {
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(context.getString(R.string.url_privacy_policy))
                        }.let { context.startActivity(it) }
                    },
                )
            }

            item {
                BasicPreference(
                    icon = { Icons.Default.Info },
                    title = { stringResource(R.string.pref_title_version) },
                    description = { BuildConfig.VERSION_NAME },
                    onClick = { logcat { "Version onClick. ${BuildConfig.VERSION_NAME}" } },
                )
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }
        }

        if (viewModel.isThemeChooserVisible.collectAsState().value) ThemeChooserDialog(
            currentTheme = { themeMode.value },
            onThemeChoose = viewModel::onThemeModeChange,
            onDismissRequest = viewModel::hideThemeChooser,
        )

        if (viewModel.isAppInstructionsVisible.collectAsState().value) SettingsDialog(
            title = { stringResource(id = R.string.pref_title_app_instructions) },
            onDismissRequest = viewModel::hideAppInstructions
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = stringResource(R.string.app_instructions),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }

        if (viewModel.isAttributionsVisible.collectAsState().value) SettingsDialog(
            title = { stringResource(id = R.string.pref_title_attributions) },
            onDismissRequest = viewModel::hideAttributions
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = stringResource(R.string.attributions),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}