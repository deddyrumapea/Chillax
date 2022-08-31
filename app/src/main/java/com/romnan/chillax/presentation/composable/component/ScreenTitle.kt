package com.romnan.chillax.presentation.composable.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.romnan.chillax.presentation.composable.theme.spacing

@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
) = Text(
    text = text,
    style = MaterialTheme.typography.h4,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colors.onBackground,
    modifier = modifier.padding(
        start = MaterialTheme.spacing.medium,
        top = MaterialTheme.spacing.large,
        end = MaterialTheme.spacing.medium,
        bottom = MaterialTheme.spacing.medium,
    ),
)