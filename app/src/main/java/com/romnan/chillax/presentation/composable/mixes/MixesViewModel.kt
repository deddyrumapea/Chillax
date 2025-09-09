package com.romnan.chillax.presentation.composable.mixes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.Mix
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.repository.MixRepository
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.presentation.composable.mixes.model.MixType
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MixesViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val mixRepository: MixRepository,
) : ViewModel() {

    private val selectedMixType = MutableStateFlow(MixesState().selectedMixType)
    private val player = playerRepository.player
    private val mixes = combineTuple(
        mixRepository.mixes,
        selectedMixType,
    ).map { (
                mixes,
                selectedMixType,
            ) ->
        mixes.filter { mix: Mix ->
            when (selectedMixType) {
                MixType.All -> true
                MixType.Preset -> !mix.isCustom
                MixType.Custom -> mix.isCustom
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )
    private val customMixToDelete = MutableStateFlow(MixesState().customMixToDelete)
    private val mixTypes = MutableStateFlow(MixesState().mixTypes)

    val state: StateFlow<MixesState> = combineTuple(
        player,
        mixes,
        customMixToDelete,
        mixTypes,
        selectedMixType,
    ).map { (
                player,
                mixes,
                customMixToDelete,
                mixTypes,
                selectedMixType,
            ) ->
        MixesState(
            player = player,
            mixes = mixes,
            customMixToDelete = customMixToDelete,
            mixTypes = mixTypes,
            selectedMixType = selectedMixType,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = MixesState(),
    )

    fun onClickDeleteMix(mix: Mix) {
        if (mix.isCustom) {
            customMixToDelete.update { mix }
        }
    }

    fun onDismissDeleteMixDialog() {
        customMixToDelete.update { null }
    }

    private var onClickConfirmDeleteMixJob: Job? = null
    fun onClickConfirmDeleteMix(mix: Mix) {
        onClickConfirmDeleteMixJob?.cancel()
        onClickConfirmDeleteMixJob = viewModelScope.launch {
            mixRepository.deleteCustomMix(mixId = mix.id)
            customMixToDelete.update { null }
        }
    }

    private var onClickPlayOrPauseJob: Job? = null
    fun onClickPlayOrPause(mix: Mix) {
        onClickPlayOrPauseJob?.cancel()
        onClickPlayOrPauseJob = viewModelScope.launch {
            val player = state.value.player

            when {
                player?.playingMix?.id == mix.id && player.phase == PlayerPhase.PLAYING -> {
                    playerRepository.pausePlayer()
                }

                else -> {
                    customMixToDelete.update { null }
                    playerRepository.addMix(mix = mix)
                }
            }
        }
    }

    fun onSelectMixType(mixType: MixType) {
        selectedMixType.update { mixType }
    }
}