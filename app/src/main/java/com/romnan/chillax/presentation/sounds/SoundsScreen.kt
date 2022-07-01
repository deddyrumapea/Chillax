package com.romnan.chillax.presentation.sounds

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.romnan.chillax.R
import com.romnan.chillax.presentation.MainViewModel
import com.romnan.chillax.presentation.model.CategoryState
import com.romnan.chillax.presentation.theme.catBgColors
import com.romnan.chillax.presentation.theme.spacing
import com.romnan.chillax.presentation.util.asString

@Composable
@Destination
@RootNavGraph(start = true) // TODO: change start destination back to MoodsScreen
fun SoundsScreen(
    viewModel: MainViewModel,
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val categories = viewModel.categories.collectAsState().value

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(id = R.string.sounds),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(
                    vertical = MaterialTheme.spacing.large,
                    horizontal = MaterialTheme.spacing.medium,
                ),
            )

            categories.forEachIndexed { catIdx: Int, category: CategoryState ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = category.readableName.asString(),
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                        color = MaterialTheme.colors.onSurface,
                    )

                    Text(
                        text = category.description.asString(),
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        items(category.sounds.size) { soundIdx ->

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                            val sound = category.sounds[soundIdx]
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(color = MaterialTheme.colors.surface)
                                    .width(100.dp)
                                    .clickable { viewModel.onSoundClicked(sound = sound) },
                            ) {
                                val bgAlpha = animateFloatAsState(
                                    targetValue = if (sound.isSelected) 1f
                                    else 0f
                                )

                                val bgColor = catBgColors[catIdx % catBgColors.size]

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .background(bgColor.copy(alpha = bgAlpha.value))
                                        .aspectRatio(1f)
                                        .fillMaxWidth()
                                ) {
                                    Icon(
                                        painter = painterResource(id = sound.iconResId),
                                        tint = MaterialTheme.colors.onSurface,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                    )
                                }

                                Divider(modifier = Modifier.fillMaxWidth())

                                Text(
                                    text = sound.readableName.asString(),
                                    color = MaterialTheme.colors.onSurface,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier
                                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                                        .fillParentMaxWidth()
                                        .padding(MaterialTheme.spacing.small),
                                )
                            }

                            if (soundIdx == category.sounds.lastIndex) Spacer(
                                modifier = Modifier.width(MaterialTheme.spacing.medium)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}