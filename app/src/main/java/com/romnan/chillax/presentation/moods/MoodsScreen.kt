package com.romnan.chillax.presentation.moods

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.romnan.chillax.R
import com.romnan.chillax.presentation.MainViewModel
import com.romnan.chillax.presentation.theme.spacing
import com.romnan.chillax.presentation.util.asString

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Destination
@RootNavGraph(start = true)
fun MoodsScreen(
    viewModel: MainViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val moods = viewModel.moods.collectAsState().value

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        Column(modifier = Modifier.padding(scaffoldPadding)) {
            // TODO: collapse toolbar on LazyGrid scroll
            Text(
                text = stringResource(id = R.string.moods),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(
                    start = MaterialTheme.spacing.medium,
                    top = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium,
                ),
            )

            // TODO: filter mood feature
            Row(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 100))
                        .background(MaterialTheme.colors.primary)
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small,
                        )
                ) {
                    Icon(imageVector = Icons.Rounded.Apps, contentDescription = null)
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Text(
                        text = stringResource(R.string.all),
                        color = MaterialTheme.colors.onPrimary,
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 100))
                        .background(MaterialTheme.colors.surface)
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small,
                        )
                ) {
                    Icon(imageVector = Icons.Default.SelfImprovement, contentDescription = null)
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Text(
                        text = stringResource(R.string.ambient),
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
            ) {
                items(moods.size) { i ->
                    val mood = moods[i]
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.small)
                            .fillParentMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { viewModel.onMoodClicked(mood = mood) }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(mood.imageResId)
                                .scale(Scale.FILL)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillParentMaxSize(),
                        )

                        Column(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colors.surface.copy(alpha = 0.2f),
                                            MaterialTheme.colors.surface,
                                        )
                                    )
                                )
                                .padding(
                                    vertical = MaterialTheme.spacing.small,
                                    horizontal = MaterialTheme.spacing.medium,
                                )
                        ) {
                            Text(
                                text = "${mood.sounds.size} sounds",
                                style = MaterialTheme.typography.caption,
                            )
                            Text(text = mood.readableName.asString())
                        }
                    }
                }
            }
        }
    }
}