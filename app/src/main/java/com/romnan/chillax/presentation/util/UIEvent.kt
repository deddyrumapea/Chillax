package com.romnan.chillax.presentation.util

import com.romnan.chillax.domain.model.UIText

sealed class UIEvent {
    data class ShowSnackbar(
        val uiText: UIText,
    ) : UIEvent()

    object HideKeyboard : UIEvent()
}