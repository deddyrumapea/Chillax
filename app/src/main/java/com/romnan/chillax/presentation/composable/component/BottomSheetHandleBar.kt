package com.romnan.chillax.presentation.composable.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetHandleBar(
    modifier: Modifier = Modifier,
    thickness: Dp = 4.dp,
    width: Dp = 48.dp,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Divider(
            thickness = thickness,
            modifier = Modifier
                .clip(RoundedCornerShape(percent = 100))
                .width(width),
        )
    }
}