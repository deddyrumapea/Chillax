package com.romnan.chillax.presentation.composable.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.domain.model.UIText
import com.romnan.chillax.domain.repository.AppSettingsRepository
import com.romnan.chillax.presentation.constant.TimeConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    private val _isThemeChooserVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isThemeChooserVisible: StateFlow<Boolean> = _isThemeChooserVisible

    private val _isAppInstructionsVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAppInstructionsVisible: StateFlow<Boolean> = _isAppInstructionsVisible

    private val _isAttributionsVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAttributionsVisible: StateFlow<Boolean> = _isAttributionsVisible

    val themeMode: StateFlow<ThemeMode> = appSettingsRepository.appSettings
        .map { it.themeMode }
        .stateIn(viewModelScope, SharingStarted.Lazily, ThemeMode.System)

    val isBedtimeActivated: StateFlow<Boolean> = appSettingsRepository.appSettings
        .map { it.bedtimeInMillis != null }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val bedtimeCalendar: StateFlow<Calendar> = appSettingsRepository.appSettings
        .map { appSettings ->
            appSettings.bedtimeInMillis?.let { millis ->
                Calendar.getInstance().apply { timeInMillis = millis }
            } ?: TimeConstants.DEFAULT_BED_TIME
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = TimeConstants.DEFAULT_BED_TIME
        )

    val bedtimeFormatted: StateFlow<UIText> = bedtimeCalendar.map {
        val sdf = SimpleDateFormat(TimeConstants.TWELVE_HOUR_FORMAT, Locale.getDefault())
        UIText.DynamicString(sdf.format(it.time))
    }.stateIn(viewModelScope, SharingStarted.Lazily, UIText.Blank)

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

    private var showAttributionsJob: Job? = null
    fun showAttributions() {
        showAttributionsJob?.cancel()
        showAttributionsJob = viewModelScope.launch {
            _isAttributionsVisible.update { true }
        }
    }

    private var hideAttributionsJob: Job? = null
    fun hideAttributions() {
        hideAttributionsJob?.cancel()
        hideAttributionsJob = viewModelScope.launch {
            _isAttributionsVisible.update { false }
        }
    }
}