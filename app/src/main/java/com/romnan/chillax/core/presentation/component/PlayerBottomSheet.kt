package com.romnan.chillax.core.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.romnan.chillax.R
import com.romnan.chillax.core.domain.model.PlayerPhase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerBottomSheet(
    playerPhase: PlayerPhase,
    bottomSheetState: BottomSheetState,
    peekHeight: Dp,
    modifier: Modifier = Modifier,
    onPlayPauseClicked: () -> Unit,
    onStopClicked: () -> Unit,
    maxHeightFraction: Float = 0.8f
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = maxHeightFraction)

    ) {
        AnimatedVisibility(visible = bottomSheetState.isCollapsed) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(peekHeight)
                    .clickable {
                        coroutineScope.launch {
                            if (bottomSheetState.isCollapsed) bottomSheetState.expand()
                        }
                    }
            ) {
                if (playerPhase != PlayerPhase.Stopped) {
                    IconButton(onClick = onPlayPauseClicked) {
                        Icon(
                            imageVector = if (playerPhase == PlayerPhase.Playing)
                                Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = stringResource(
                                if (playerPhase == PlayerPhase.Playing)
                                    R.string.pause else R.string.play
                            )
                        )
                    }

                    IconButton(onClick = onStopClicked) {
                        Icon(
                            imageVector = Icons.Default.StopCircle,
                            contentDescription = stringResource(R.string.stop)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "BottomSheet content")
        }
    }
}