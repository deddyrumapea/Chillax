package com.romnan.chillax.presentation.composable.settings

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.core.net.toUri

@Composable
fun attributionsAnnotatedString(): AnnotatedString {
    val linkInteractionListener = AttributionsLinkInteractionListener(
        context = LocalContext.current,
    )

    return buildAnnotatedString {
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/air-conditioner",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nAir conditioner icons created by Freepik - Flaticon") }
    }
}

class AttributionsLinkInteractionListener(
    val context: Context,
) : LinkInteractionListener {
    override fun onClick(link: LinkAnnotation) {
        val url = (link as? LinkAnnotation.Url)?.url ?: return

        try {
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(context, url.toUri())
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
        }

    }
}