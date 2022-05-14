package com.romnan.chillax.featSounds.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.romnan.chillax.core.presentation.MainViewModel

@Composable
@Destination
@RootNavGraph(start = true) // TODO: change start destination back to MoodsScreen
fun SoundsScreen(
    viewModel: MainViewModel
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column {
            Text(text = "SoundsScreen")

            viewModel.soundsList.collectAsState().value.forEach {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor =
                        if (!it.isSelected) MaterialTheme.colors.primary
                        else MaterialTheme.colors.surface
                    ),
                    onClick = { viewModel.onSoundClicked(it) }
                ) {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = stringResource(id = it.name),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = stringResource(id = it.name))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = it.isSelected.toString())
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}