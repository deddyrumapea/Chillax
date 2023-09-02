package com.romnan.chillax.presentation.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.romnan.chillax.R
import com.romnan.chillax.presentation.composable.theme.spacing

@Composable
fun DefaultDialog(
    title: @Composable () -> String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Column(
            modifier = modifier.background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(8.dp),
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MaterialTheme.spacing.extraSmall,
                        bottom = MaterialTheme.spacing.extraSmall,
                        start = MaterialTheme.spacing.medium,
                    ),
            ) {
                Text(
                    text = title(),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f),
                )

                IconButton(onClick = { onDismissRequest() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cd_close),
                    )
                }
            }

            Divider()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
            ) {
                content.invoke(this)
            }
        }
    }
}