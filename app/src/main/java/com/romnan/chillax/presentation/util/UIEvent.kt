package com.romnan.chillax.presentation.util


import android.widget.Toast
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
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

    data class ShowSnackbar(
        val uiText: UIText,
        val actionLabel: String? = null,
        val duration: SnackbarDuration = when (actionLabel == null) {
            true -> SnackbarDuration.Short
            false -> SnackbarDuration.Indefinite
        },
    ) : UIEvent()

    data object ClearFocus : UIEvent()
}

/**
 * Handles a Flow of UI events in a Composable LaunchedEffect, allowing you to react to
 * specific UI events.
 *
 * @param snackbarHostState The SnackbarHostState to use for showing Snackbars.
 */
@Composable
fun Flow<UIEvent>.handleInLaunchedEffect(
    snackbarHostState: SnackbarHostState,
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

                is UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context),
                        actionLabel = event.actionLabel,
                        duration = event.duration,
                    )
                }

                is UIEvent.ClearFocus -> {
                    focusManager.clearFocus()
                }
            }

            onAfterHandled(event)
        }
    }
}