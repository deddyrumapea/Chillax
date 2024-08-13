package com.romnan.chillax.presentation.composable.component

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canhub.cropper.CropException
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.romnan.chillax.R
import com.romnan.chillax.presentation.SaveMoodDialogState
import com.romnan.chillax.presentation.composable.theme.Pink400
import com.romnan.chillax.presentation.composable.theme.spacing
import kotlinx.coroutines.launch
import logcat.asLog
import logcat.logcat

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun SaveMoodDialog(
    state: SaveMoodDialogState,
    onDismissRequest: () -> Unit,
    onClickConfirmSaveMood: (
        readableName: String,
        imageUri: String?,
    ) -> Unit,
    onPickNewMoodCustomImage: suspend (uri: Uri) -> Uri,
    onClickRemoveCustomImage: (imageUri: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var imageUriToShowDeleteButton by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val imagePicker = rememberLauncherForActivityResult(
        contract = CropImageContract(),
        onResult = { result: CropImageView.CropResult? ->
            when (val uri = result?.uriContent) {
                null -> {
                    logcat("SaveMoodDialog") { result?.error?.asLog().orEmpty() }

                    when (result?.error) {
                        is CropException.Cancellation -> {
                            // User cancelled the operation
                        }

                        is CropException.FailedToDecodeImage -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.failed_to_decode_image_please_try_again),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }

                        is CropException.FailedToLoadBitmap -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.failed_to_load_image_please_try_again),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.failed_to_pick_image_please_try_again),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
                }

                else -> {
                    coroutineScope.launch {
                        selectedImageUri = onPickNewMoodCustomImage(uri).toString()
                    }
                }
            }
        },
    )

    DefaultDialog(
        title = { stringResource(R.string.save_new_mood) },
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium - MaterialTheme.spacing.extraSmall),
        ) {
            item(
                span = { GridItemSpan(currentLineSpan = 4) },
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.spacing.extraSmall,
                            vertical = MaterialTheme.spacing.medium,
                        ),
                    singleLine = true,
                    label = { Text(text = stringResource(R.string.mood_name)) },
                    colors = TextFieldDefaults.textFieldColors(
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(16.dp),
                )
            }

            items(
                items = state.moodPresetImageUris.toList(),
                key = { imageUri: String -> imageUri },
            ) { imageUri: String ->
                Box(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .padding(MaterialTheme.spacing.extraSmall)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 3.dp,
                            color = when (selectedImageUri == imageUri) {
                                true -> MaterialTheme.colors.primary
                                false -> Color.Transparent
                            },
                            shape = RoundedCornerShape(16.dp),
                        )
                        .clickable {
                            imageUriToShowDeleteButton = null
                            selectedImageUri = imageUri
                        },
                ) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        colorFilter = when (selectedImageUri == imageUri) {
                            true -> ColorFilter.tint(
                                Color.Black.copy(alpha = 0.5f),
                                BlendMode.Multiply,
                            )

                            false -> null
                        },
                    )

                    if (selectedImageUri == imageUri) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                            tint = MaterialTheme.colors.primary,
                        )
                    }
                }
            }

            items(
                items = state.moodCustomImageUris.toList(),
                key = { imageUri: String -> imageUri },
            ) { imageUri: String ->
                Box(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .padding(MaterialTheme.spacing.extraSmall / 2),
                ) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.extraSmall / 2)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 3.dp,
                                color = when (selectedImageUri == imageUri) {
                                    true -> MaterialTheme.colors.primary
                                    false -> Color.Transparent
                                },
                                shape = RoundedCornerShape(16.dp),
                            )
                            .combinedClickable(
                                onClick = {
                                    imageUriToShowDeleteButton = null
                                    selectedImageUri = imageUri
                                },
                                onLongClick = {
                                    selectedImageUri = null
                                    imageUriToShowDeleteButton = imageUri
                                },
                            ),
                        contentScale = ContentScale.Crop,
                        colorFilter = when (selectedImageUri == imageUri) {
                            true -> ColorFilter.tint(
                                Color.Black.copy(alpha = 0.5f),
                                BlendMode.Multiply,
                            )

                            false -> null
                        },
                    )

                    if (selectedImageUri == imageUri) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                            tint = MaterialTheme.colors.primary,
                        )
                    }

                    if (imageUriToShowDeleteButton == imageUri) {
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentEnforcement provides false
                        ) {
                            IconButton(
                                onClick = {
                                    selectedImageUri = null
                                    imageUriToShowDeleteButton = null
                                    onClickRemoveCustomImage(imageUri)
                                },
                                modifier = Modifier.align(Alignment.TopEnd),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircle,
                                    contentDescription = null,
                                    tint = Pink400,
                                    modifier = Modifier.background(
                                        color = Color.White,
                                        shape = CircleShape,
                                    ),
                                )
                            }
                        }
                    }
                }
            }

            item(
                key = "item_add_custom_image",
            ) {
                Box(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .padding(MaterialTheme.spacing.extraSmall)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                        .clickable {
                            imagePicker.launch(
                                CropImageContractOptions(
                                    uri = null,
                                    cropImageOptions = CropImageOptions(
                                        fixAspectRatio = true,
                                    ),
                                )
                            )
                        },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.cd_add_image),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }

            item(
                span = { GridItemSpan(currentLineSpan = 4) },
            ) {
                Button(
                    onClick = {
                        onClickConfirmSaveMood(
                            name,
                            selectedImageUri,
                        )
                    },
                    modifier = Modifier
                        .padding(
                            horizontal = MaterialTheme.spacing.extraSmall,
                            vertical = MaterialTheme.spacing.medium,
                        )
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(100),
                ) {
                    Text(
                        text = stringResource(R.string.save).uppercase(),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}