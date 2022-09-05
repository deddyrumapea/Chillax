package com.romnan.chillax.presentation.composable.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
    style = MaterialTheme.typography.h4,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colors.onBackground,
    modifier = modifier.padding(paddingValues),
)