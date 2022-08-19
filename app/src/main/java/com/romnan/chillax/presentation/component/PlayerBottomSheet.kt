package com.romnan.chillax.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.PlayerState
import com.romnan.chillax.presentation.theme.spacing
import com.romnan.chillax.presentation.util.asString

@Composable
fun PlayerBottomSheet(
    playerState: PlayerState,
    peekHeight: Dp,
    isCollapsed: Boolean,
    modifier: Modifier = Modifier,
    onPeekClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onStopClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(MaterialTheme.colors.surface),
    ) {
        PlayerBottomSheetPeek(
            titleText = playerState.playingSoundsTitle.asString(),
            isPlaying = playerState.phase == PlayerPhase.PLAYING,
            height = peekHeight,
            onPlayPauseClick = onPlayPauseClick,
            modifier = if (isCollapsed) Modifier.clickable { onPeekClick() } else Modifier
        )

        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            for (sound in playerState.playingSounds) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = sound.iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Slider(
                        value = 0.5f,
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Stop ${sound.readableName.asString()}",
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onStopClick,
                    shape = RoundedCornerShape(percent = 100),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = MaterialTheme.colors.primary,
                    ),
                    border = ButtonDefaults.outlinedBorder.copy(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colors.primary,
                                MaterialTheme.colors.primaryVariant,
                            )
                        )
                    )
                ) {
                    Icon(imageVector = Icons.Outlined.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Text(
                        text = "Stop",
                        style = MaterialTheme.typography.button,
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                Button(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(percent = 100),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.button,
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}