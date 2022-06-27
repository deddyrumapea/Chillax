package com.romnan.chillax.presentation.moods

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.chillax.presentation.MainViewModel

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
                    onClick = { viewModel.onMoodClicked(it) }
                ) {
                    Text(text = stringResource(id = it.name))
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}