package com.romnan.chillax.featMoods.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.core.presentation.MainViewModel

@Composable
@Destination
fun MoodsScreen(
    viewModel: MainViewModel
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column {
            Text(text = "MoodsScreen")

            viewModel.moodsList.collectAsState().value.forEach {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor =
                        if (!it.isPlaying) MaterialTheme.colors.primary
                        else MaterialTheme.colors.surface
                    ),
                    onClick = { viewModel.onMoodClicked(it) }
                ) {
                    Text(text = stringResource(id = it.name))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = it.isPlaying.toString())
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}