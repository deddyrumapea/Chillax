package com.romnan.chillax.presentation.composable.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.romnan.chillax.presentation.composable.theme.spacing

@Composable
fun ScreenTitle(
    text: @Composable () -> String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(
        start = MaterialTheme.spacing.medium,
        top = MaterialTheme.spacing.large,
        end = MaterialTheme.spacing.medium,
        bottom = MaterialTheme.spacing.medium,
    ),
) = Text(
    text = text(),
    style = MaterialTheme.typography.headlineMedium,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onBackground,
    modifier = modifier.padding(paddingValues),
)