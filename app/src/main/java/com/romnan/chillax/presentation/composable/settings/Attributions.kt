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
        ) { append("Air conditioner icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/bird",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nBird icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/mountain",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nMountain icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/plague",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nPlague icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/waves",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nWaves icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/cricket",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nCricket icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/fireplace",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nFireplace icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/wind",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nWind icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/frog",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nFrog icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/rain",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nRain icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/plane",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nPlane icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/roof",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nRoof icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/tent",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nTent icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/trees",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nTrees icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/sea",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nSea icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/thunder",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nThunder icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/train",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nTrain icons created by Freepik - Flaticon") }
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