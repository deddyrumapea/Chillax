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
                url = "https://www.flaticon.com/free-icons/airplane",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Airplane icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/bird",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nBird icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/cicada",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nCicada icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/creek",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nCreek icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/fireplace",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nFireplace icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/frog",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nFrog icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/group",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nGroup icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/heart",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nHeart icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/insect",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nInsect icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/keyboard",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nKeyboard icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/mountain",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nMountain icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/pan",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nPan icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/park",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nPark icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/pets",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nPets icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/plane",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nPlane icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/playground",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nPlayground icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/radio",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nRadio icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/rain",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nRain icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/roof",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nRoof icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/seagull",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nSeagull icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/steering-wheel",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nSteering wheel icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/train",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nTrain icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/trees",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nTrees icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/umbrella",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nUmbrella icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/waterfall",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nWaterfall icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/wave",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nWave icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/waves",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nWaves icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/window-ac",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("\nWindow ac icons created by Freepik - Flaticon") }

        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/wiper",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Wiper icons created by Freepik - Flaticon") }
    }
}

class AttributionsLinkInteractionListener(
    val context: Context,
) : LinkInteractionListener {
    override fun onClick(link: LinkAnnotation) {
        val url = (link as? LinkAnnotation.Url)?.url ?: return
        openUrl(url = url)

    }

    private fun openUrl(url: String) {
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