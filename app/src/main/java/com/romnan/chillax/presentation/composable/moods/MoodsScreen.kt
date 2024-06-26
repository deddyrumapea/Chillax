package com.romnan.chillax.presentation.composable.moods

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.romnan.chillax.R
import com.romnan.chillax.presentation.composable.component.ScreenTitle
import com.romnan.chillax.presentation.composable.moods.component.MoodItem
import com.romnan.chillax.presentation.composable.theme.spacing

@Composable
@Destination
@RootNavGraph(start = true)
fun MoodsScreen(
    viewModel: MoodsViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val moods = viewModel.moods.collectAsState().value

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(horizontal = MaterialTheme.spacing.small),
        ) {
            item(span = { GridItemSpan(currentLineSpan = 2) }) {
                ScreenTitle(
                    text = { stringResource(id = R.string.moods) },
                    paddingValues = PaddingValues(
                        start = MaterialTheme.spacing.small,
                        top = MaterialTheme.spacing.large,
                        end = MaterialTheme.spacing.small,
                        bottom = MaterialTheme.spacing.medium,
                    ),
                )
            }

            items(
                count = moods.size,
                key = { i -> moods[i].name },
            ) { i ->
                MoodItem(
                    mood = { moods[i] },
                    onClick = viewModel::onMoodClick,
                    modifier = Modifier.padding(MaterialTheme.spacing.small),
                )
            }

            item(span = { GridItemSpan(currentLineSpan = 2) }) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            }
        }
    }
}