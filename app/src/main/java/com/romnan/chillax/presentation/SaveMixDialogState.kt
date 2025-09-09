package com.romnan.chillax.presentation

data class SaveMixDialogState(
    val showSaveMixDialog: Boolean = false,
    val mixPresetImageUris: Set<String> = emptySet(),
    val mixCustomImageUris: Set<String> = emptySet(),
    val mixCustomImageUriToDelete: String? = null,
)