package com.romnan.chillax.domain.model

import androidx.annotation.StringRes

sealed class UIText {
    data class DynamicString(val value: String) : UIText()
    class StringResource(@StringRes val id: Int, vararg val formatArgs: Any) : UIText()
    object Blank : UIText()
}