package com.romnan.chillax.presentation.util


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.romnan.chillax.domain.model.UIText

@Composable
fun UIText.asString(): String {
    return when (this) {
        is UIText.DynamicString -> this.value
        is UIText.StringResource -> stringResource(id = this.id, *this.args)
        is UIText.PluralsResource -> LocalContext.current.resources.getQuantityString(
            this.id,
            this.quantity,
            *this.args,
        )

        is UIText.Blank -> ""
    }
}

fun UIText.asString(context: Context): String {
    return when (this) {
        is UIText.DynamicString -> this.value
        is UIText.StringResource -> context.getString(this.id, *this.args)
        is UIText.PluralsResource -> context.resources.getQuantityString(
            this.id,
            this.quantity,
            *this.args,
        )

        is UIText.Blank -> ""
    }
}