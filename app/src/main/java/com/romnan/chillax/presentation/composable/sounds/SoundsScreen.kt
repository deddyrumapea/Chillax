package com.romnan.chillax.presentation.composable.sounds

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.R
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.sounds.component.CategoryItem
import com.romnan.chillax.presentation.composable.theme.catBgColors
import com.romnan.chillax.presentation.composable.theme.spacing

@Composable
@Destination
fun SoundsScreen(
    viewModel: SoundsViewModel,
) {
    val scaffoldState = rememberScaffoldState()
    val categories = viewModel.categories.collectAsState().value

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(scaffoldPadding)
        ) {
            item { ScreenTitle(text = { stringResource(id = R.string.sounds) }) }

            items(
                count = categories.size,
                key = { i -> categories[i].name },
            ) { i ->
                CategoryItem(
                    category = { categories[i] },
                    soundActiveBgColor = { catBgColors[i % catBgColors.size] },
                    onSoundClick = { sound -> viewModel.onSoundClick(sound = sound) },
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }
        }
    }
}