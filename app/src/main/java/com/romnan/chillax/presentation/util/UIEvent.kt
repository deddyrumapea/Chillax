package com.romnan.chillax.presentation.util


import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.romnan.chillax.domain.model.UIText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

sealed class UIEvent {
    data class ShowToast(
        val uiText: UIText,
    ) : UIEvent()

    data object ClearFocus : UIEvent()
}

@Composable
fun Flow<UIEvent>.handleInLaunchedEffect(
    onAfterHandled: (UIEvent) -> Unit = {},
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        this@handleInLaunchedEffect.collectLatest { event ->
            when (event) {
                is UIEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.uiText.asString(context),
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                is UIEvent.ClearFocus -> {
                    focusManager.clearFocus()
                }
            }

            onAfterHandled(event)
        }
    }
}