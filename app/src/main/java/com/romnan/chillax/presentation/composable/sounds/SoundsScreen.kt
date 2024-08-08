package com.romnan.chillax.presentation.composable.sounds

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.R
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.sounds.component.CategoryItem
import com.romnan.chillax.presentation.composable.theme.catBgColors
import com.romnan.chillax.presentation.composable.theme.spacing
import com.romnan.chillax.presentation.model.CategoryPresentation

@Composable
@Destination
fun SoundsScreen(
    viewModel: SoundsViewModel,
) {
    val categories by viewModel.categories.collectAsState()

    Scaffold(
        scaffoldState = rememberScaffoldState(),
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(scaffoldPadding)
        ) {
            item {
                ScreenTitle(
                    text = { stringResource(id = R.string.sounds) },
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            }

            itemsIndexed(
                items = categories,
                key = { i: Int, category: CategoryPresentation -> category.id }
            ) { i: Int, category: CategoryPresentation ->
                CategoryItem(
                    category = { category },
                    soundActiveBgColor = { catBgColors[i % catBgColors.size] },
                    onClickSound = viewModel::onClickSound,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }
        }
    }
}