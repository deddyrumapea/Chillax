package com.romnan.chillax.domain.model

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class UIText {
    data class DynamicString(val value: String) : UIText()

    class StringResource(
        @StringRes val id: Int,
        vararg formatArgs: Any,
    ) : UIText() {

        val args: Array<out Any> = formatArgs

        override fun toString(): String {
            val argsString = args.joinToString()
            return "StringResource(id=$id, formatArgs=[$argsString])"
        }
    }

    class PluralsResource(
        @PluralsRes val id: Int,
        val quantity: Int,
        vararg formatArgs: Any,
    ) : UIText() {

        val args: Array<out Any> = formatArgs

        override fun toString(): String {
            val argsString = args.joinToString()
            return "PluralsResource(id=$id, quantity=$quantity, formatArgs=[$argsString])"
        }
    }

    data object Blank : UIText()
}

fun String.toUIText() = UIText.DynamicString(this)