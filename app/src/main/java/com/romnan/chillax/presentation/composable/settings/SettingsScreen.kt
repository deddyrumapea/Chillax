package com.romnan.chillax.presentation.composable.settings

import android.app.TimePickerDialog
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.BuildConfig
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.settings.component.BasicPreference
import com.romnan.chillax.presentation.composable.settings.component.SettingsDialog
import com.romnan.chillax.presentation.composable.settings.component.SwitchPreference
import com.romnan.chillax.presentation.composable.settings.component.ThemeChooserDialog
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.constant.IntentConstants
import com.romnan.chillax.presentation.util.asString
import logcat.logcat
import java.util.*

@Composable
@Destination
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        val themeMode = viewModel.themeMode.collectAsState()

        val bedTimeCal = viewModel.bedTimeCalendar.collectAsState().value
        val timePickerDialog = TimePickerDialog(
            LocalContext.current,
            { _, hourOfDay: Int, minute: Int ->
                viewModel.onBedTimePicked(hourOfDay = hourOfDay, minute = minute)
            },
            bedTimeCal[Calendar.HOUR_OF_DAY],
            bedTimeCal[Calendar.MINUTE],
            false
        )

        LazyColumn(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            item { ScreenTitle(text = stringResource(id = R.string.settings)) }

            item {
                BasicPreference(
                    imageVector = when (themeMode.value) {
                        ThemeMode.System -> Icons.Default.BrightnessMedium
                        ThemeMode.Light -> Icons.Default.LightMode
                        ThemeMode.Dark -> Icons.Default.DarkMode
                    },
                    title = stringResource(R.string.pref_title_theme),
                    description = themeMode.value.readableName.asString(),
                    onClick = viewModel::showThemeChooser,
                )
            }

            item {
                val isBedTimeActivated = viewModel.isBedTimeActivated.collectAsState().value
                SwitchPreference(
                    imageVector =
                    if (isBedTimeActivated) Icons.Default.NotificationsActive
                    else Icons.Default.Notifications,
                    title = stringResource(R.string.pref_title_bedtime_reminder),
                    description = viewModel.bedTimeFormatted.collectAsState().value.asString(),
                    checked = isBedTimeActivated,
                    onClick = {
                        if (!isBedTimeActivated) timePickerDialog.show()
                        else viewModel.onTurnOffBedTime()
                    },
                    onCheckedChange = {
                        if (!isBedTimeActivated) timePickerDialog.show()
                        else viewModel.onTurnOffBedTime()
                    }
                )
            }

            item { Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small)) }

            item {
                BasicPreference(
                    imageVector = Icons.Default.RateReview,
                    title = stringResource(R.string.pref_title_rate_app),
                    description = stringResource(R.string.pref_desc_rate_app),
                    onClick = {
                        Intent(Intent.ACTION_VIEW)
                            .apply { data = Uri.parse(context.getString(R.string.url_app_listing)) }
                            .let { context.startActivity(it) }
                    },
                )
            }

            item {
                BasicPreference(
                    imageVector = Icons.Default.Share,
                    title = stringResource(R.string.pref_title_share_app),
                    description = stringResource(R.string.pref_desc_share_app),
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
                    imageVector = Icons.Default.ContactMail,
                    title = stringResource(R.string.pref_title_contact_support),
                    description = stringResource(R.string.pref_desc_contact_support),
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
                    imageVector = Icons.Default.IntegrationInstructions,
                    title = stringResource(R.string.pref_title_app_instructions),
                    onClick = { viewModel.showAppInstructions() },
                )
            }

            item {
                BasicPreference(
                    imageVector = Icons.Default.Attribution,
                    title = stringResource(R.string.pref_title_attributions),
                    onClick = { viewModel.showAttributions() },
                )
            }

            item {
                BasicPreference(
                    imageVector = Icons.Default.PrivacyTip,
                    title = stringResource(R.string.pref_title_privacy_policy),
                    onClick = {
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(context.getString(R.string.url_privacy_policy))
                        }.let { context.startActivity(it) }
                    },
                )
            }

            item {
                BasicPreference(
                    imageVector = Icons.Default.Info,
                    title = stringResource(R.string.pref_title_version),
                    description = BuildConfig.VERSION_NAME,
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
            title = stringResource(id = R.string.pref_title_app_instructions),
            onDismissRequest = viewModel::hideAppInstructions
        ) {
            Text(
                text = stringResource(R.string.app_instructions),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }

        if (viewModel.isAttributionsVisible.collectAsState().value) SettingsDialog(
            title = stringResource(id = R.string.pref_title_attributions),
            onDismissRequest = viewModel::hideAttributions
        ) {
            Text(
                text = stringResource(R.string.attributions),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}