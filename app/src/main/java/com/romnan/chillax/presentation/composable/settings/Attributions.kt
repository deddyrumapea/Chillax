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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.core.net.toUri

@Composable
fun attributionsAnnotatedString(): AnnotatedString {
    val linkInteractionListener = AttributionsLinkInteractionListener(
        context = LocalContext.current,
    )

    return buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Sound Icons")
        }
        append("\n\n")

        // Sound Icons
        append("Airplane icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/airplane",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Bird icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/bird",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Cicada icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/cicada",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Creek icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/creek",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Fireplace icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/fireplace",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Frog icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/frog",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Group icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/group",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Heart icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/heart",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Insect icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/insect",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Keyboard icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/keyboard",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Mountain icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/mountain",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Pan icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/pan",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Park icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/park",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Pets icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/pets",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Plane icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/plane",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Playground icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/playground",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Radio icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/radio",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Rain icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/rain",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Roof icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/roof",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Seagull icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/seagull",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Steering wheel icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/steering-wheel",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Train icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/train",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Trees icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/trees",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Umbrella icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/umbrella",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Waterfall icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/waterfall",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Wave icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/wave",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Waves icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/waves",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Window ac icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/window-ac",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n")

        append("Wiper icons created by Freepik - ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.flaticon.com/free-icons/wiper",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Flaticon") }
        append("\n\n")

        // Sound Audios section
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Sound Audios")
        }
        append("\n\n")

        // Sound Audios
        append("Birds 1: Sound Effect by ivolipa from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Birds 2: Sound Effect by nektaria909 from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Birds 3: Sound Effect by swiftoid from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Brook: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Brown Noise: Sound Effect by digitalspa from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Cat Purring: Sound Effect by worldsday from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Cicadas: Sound Effect by sarvegu from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Creek: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Crickets: Sound Effect by cclaretc from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Crowd: Sound Effect by karinalarasart from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Deep Frying: Sound Effect by juliush from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Driving at Night: Sound Effect by augustsandberg from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Electricity: Sound Effect by flashtrauma from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Fireplace: Sound Effect by juliush from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Frogs1: Sound Effect by jayalvarez66 from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Frogs2: Sound Effect by zachrau from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Heartbeat: Sound Effect by placidplace from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Jet Plane: Sound Effect by habbis92 from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Keyboard: Sound Effect by kevinchocs from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://www.youtube.com/@kevinchocs/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("YouTube") }
        append("\n")

        append("Kids Playground: Sound Effect by brunoauzet from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Radio Static: Sound Effect by theartguild from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Rain Gentle: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Rain Umbrella: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Rain on Metal Roof: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Rain: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("River: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Sea Waves: Sound Effect by soundsforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Seagulls: Sound Effect by olesouwester from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Thunder: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Train: Sound Effect by sspsurvival from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Turboprop plane: Sound Effect by daveshu88 from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Waterfall: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Wind in Trees: Sound Effect by soundforyou from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Window AC: Sound Effect by benhabrams from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")

        append("Windshield Wipers: Sound Effect by beeproductive from ")
        withLink(
            link = LinkAnnotation.Url(
                url = "https://pixabay.com/",
                linkInteractionListener = linkInteractionListener,
                styles = TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline)),
            ),
        ) { append("Pixabay") }
        append("\n")
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