package com.romnan.chillax.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.R
import com.romnan.chillax.presentation.settings.component.BasicPreference
import com.romnan.chillax.presentation.settings.component.SwitchPreference
import com.romnan.chillax.presentation.theme.spacing
import logcat.logcat

@Composable
@Destination
fun SettingsScreen() {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    // TODO: implement settings feature
    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(
                    start = MaterialTheme.spacing.medium,
                    top = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium,
                ),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            BasicPreference(
                imageVector = Icons.Default.DarkMode,
                title = "Theme",
                description = "Dark",
                onClick = { logcat { "Theme clicked" } },
            )

            SwitchPreference(
                imageVector = Icons.Default.Timer,
                title = "Remind me when it's bedtime",
                description = "Off",
                checked = false,
                onClick = { logcat { "Bedtime clicked" } },
                onCheckedChange = { logcat { "Bedtime chekedchange" } }
            )

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            BasicPreference(
                imageVector = Icons.Default.RateReview,
                title = "Rate this app",
                description = "Tell us how do you like this app",
                onClick = { logcat { "Theme clicked" } },
            )

            BasicPreference(
                imageVector = Icons.Default.Share,
                title = "Share this app",
                description = "Help spread the word to your friends",
                onClick = { logcat { "Share clicked" } },
            )

            BasicPreference(
                imageVector = Icons.Default.ContactMail,
                title = "Contact support",
                description = "Report a problem or give suggestions",
                onClick = { logcat { "Theme clicked" } },
            )

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            BasicPreference(
                imageVector = Icons.Default.IntegrationInstructions,
                title = "Show app instructions",
                onClick = { logcat { "Theme clicked" } },
            )

            BasicPreference(
                imageVector = Icons.Default.PrivacyTip,
                title = "Privacy policy",
                onClick = { logcat { "Theme clicked" } },
            )

            BasicPreference(
                imageVector = Icons.Default.Attribution,
                title = "Attributions",
                onClick = { logcat { "Theme clicked" } },
            )

            BasicPreference(
                imageVector = Icons.Default.Info,
                title = "Version",
                description = "2022.07.02",
                onClick = { logcat { "Theme clicked" } },
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}