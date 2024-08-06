package com.romnan.chillax.presentation.composable.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.domain.model.UIText
import com.romnan.chillax.domain.repository.AppSettingsRepository
import com.romnan.chillax.presentation.model.BedtimePresentation
import com.romnan.chillax.presentation.model.toBedtimePresentation
import com.romnan.chillax.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    private val _isThemeChooserVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isThemeChooserVisible: StateFlow<Boolean> = _isThemeChooserVisible

    private val _isAppInstructionsVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAppInstructionsVisible: StateFlow<Boolean> = _isAppInstructionsVisible

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val themeMode: StateFlow<ThemeMode> = appSettingsRepository.appSettings.map { it.themeMode }
        .stateIn(viewModelScope, SharingStarted.Lazily, ThemeMode.System)

    val bedtime: StateFlow<BedtimePresentation> =
        appSettingsRepository.appSettings.map { it.bedtimeInMillis.toBedtimePresentation() }
            .stateIn(viewModelScope, SharingStarted.Lazily, BedtimePresentation.defaultValue)

    private val _visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())
    val visiblePermissionDialogQueue: StateFlow<List<String>> = _visiblePermissionDialogQueue

    private var onThemeModeChangeJob: Job? = null
    fun onThemeModeChange(themeMode: ThemeMode) {
        onThemeModeChangeJob?.cancel()
        onThemeModeChangeJob = viewModelScope.launch {
            hideThemeChooser()
            appSettingsRepository.setThemeMode(themeMode = themeMode)
        }
    }

    private var onBedtimePickedJob: Job? = null
    fun onBedtimePicked(hourOfDay: Int, minute: Int) {
        onBedtimePickedJob?.cancel()
        onBedtimePickedJob = viewModelScope.launch {
            val timeInMillis = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }.timeInMillis

            appSettingsRepository.setBedtime(timeInMillis = timeInMillis)
        }
    }

    private var onTurnOffBedtimeJob: Job? = null
    fun onTurnOffBedtime() {
        onTurnOffBedtimeJob?.cancel()
        onTurnOffBedtimeJob = viewModelScope.launch {
            appSettingsRepository.setBedtime(timeInMillis = null)
        }
    }

    private var showThemeChooserJob: Job? = null
    fun showThemeChooser() {
        showThemeChooserJob?.cancel()
        showThemeChooserJob = viewModelScope.launch {
            _isThemeChooserVisible.update { true }
        }
    }

    private var hideThemeChooserJob: Job? = null
    fun hideThemeChooser() {
        hideThemeChooserJob?.cancel()
        hideThemeChooserJob = viewModelScope.launch {
            _isThemeChooserVisible.update { false }
        }
    }

    private var showAppInstructionsJob: Job? = null
    fun showAppInstructions() {
        showAppInstructionsJob?.cancel()
        showAppInstructionsJob = viewModelScope.launch {
            _isAppInstructionsVisible.update { true }
        }
    }

    private var hideAppInstructionsJob: Job? = null
    fun hideAppInstructions() {
        hideAppInstructionsJob?.cancel()
        hideAppInstructionsJob = viewModelScope.launch {
            _isAppInstructionsVisible.update { false }
        }
    }

    private var onClickAppVersionJob: Job? = null
    fun onClickAppVersion() {
        onClickAppVersionJob?.cancel()
        onClickAppVersionJob = viewModelScope.launch {
            _uiEvent.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.app_developed_by)))
        }
    }

    fun onPermissionResults(permission: String, isGranted: Boolean) {
        if (!isGranted && !visiblePermissionDialogQueue.value.contains(permission)) {
            _visiblePermissionDialogQueue.update { it.toMutableList().apply { add(permission) } }
        }
    }

    fun onDismissPermissionDialog() {
        _visiblePermissionDialogQueue.update { it.toMutableList().apply { removeFirst() } }
    }
}