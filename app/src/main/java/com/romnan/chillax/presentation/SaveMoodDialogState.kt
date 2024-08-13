package com.romnan.chillax.presentation

data class SaveMoodDialogState(
    val showSaveMoodDialog: Boolean = false,
    val moodPresetImageUris: Set<String> = emptySet(),
    val moodCustomImageUris: Set<String> = emptySet(),
    val moodCustomImageUriToDelete: String? = null,
)