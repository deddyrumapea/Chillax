package com.romnan.chillax.presentation.composable.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.BuildConfig
import com.romnan.chillax.R
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.settings.component.BasicPreference
import com.romnan.chillax.presentation.composable.settings.component.SwitchPreference
import com.romnan.chillax.presentation.composable.theme.spacing
import logcat.logcat

@Composable
@Destination
fun SettingsScreen() {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            item { ScreenTitle(text = stringResource(id = R.string.settings)) }

            item {
                BasicPreference(
                    imageVector = Icons.Default.DarkMode,
                    title = stringResource(R.string.pref_title_theme),
                    description = stringResource(R.string.pref_desc_dark),
                    onClick = { logcat { "Theme onClick" } },
                )
            }

            item {
                SwitchPreference(
                    imageVector = Icons.Default.Timer,
                    title = stringResource(R.string.pref_title_bedtime_reminder),
                    description = stringResource(R.string.off),
                    checked = false,
                    onClick = { logcat { "Bedtime onClick" } },
                    onCheckedChange = { logcat { "Bedtime onCheckedChange" } }
                )
            }

            item { Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small)) }

            item {
                BasicPreference(
                    imageVector = Icons.Default.RateReview,
                    title = stringResource(R.string.pref_title_rate_app),
                    description = stringResource(R.string.pref_desc_rate_app),
                    onClick = { logcat { "RateApp onClick" } },
                )
            }

            item {
                BasicPreference(
                    imageVector = Icons.Default.Share,
                    title = stringResource(R.string.pref_title_share_app),
                    description = stringResource(R.string.pref_desc_share_app),
                    onClick = { logcat { "Share onClick" } },
                )
            }

            item {
                BasicPreference(
                    imageVector = Icons.Default.ContactMail,
                    title = stringResource(R.string.pref_title_contact_support),
                    description = stringResource(R.string.pref_desc_contact_support),
                    onClick = { logcat { "Contact support onClick" } },
                )
            }

            item { Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small)) }

            item {
                BasicPreference(
                    imageVector = Icons.Default.IntegrationInstructions,
                    title = stringResource(R.string.pref_title_app_instructions),
                    onClick = { logcat { "App Instructions onClick" } },
                )
            }

            item {
                BasicPreference(
                    imageVector = Icons.Default.PrivacyTip,
                    title = stringResource(R.string.pref_title_privacy_policy),
                    onClick = { logcat { "Privacy policy onClick" } },
                )
            }

            item {
                BasicPreference(
                    imageVector = Icons.Default.Attribution,
                    title = stringResource(R.string.pref_title_attributions),
                    onClick = { logcat { "Attributions clicked" } },
                )
            }

            item {
                BasicPreference(
                    imageVector = Icons.Default.Info,
                    title = stringResource(R.string.pref_title_version),
                    description = BuildConfig.VERSION_NAME,
                    onClick = { logcat { "Version onClick" } },
                )
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }
        }
    }
}