package com.romnan.chillax.presentation.composable.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.util.asString

@Composable
fun ThemeChooserDialog(
    currentTheme: () -> ThemeMode,
    onThemeChoose: (ThemeMode) -> Unit,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    SettingsDialog(
        title = stringResource(id = R.string.choose_theme),
        onDismissRequest = onDismissRequest,
    ) {
        ThemeMode.values().forEach { themeMode ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeChoose(themeMode) }
                    .padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                RadioButton(
                    selected = themeMode == currentTheme(),
                    onClick = { onThemeChoose(themeMode) },
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(text = themeMode.readableName.asString())
            }
        }
    }
}