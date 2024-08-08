package com.romnan.chillax.presentation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.FloatRange
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.romnan.chillax.data.model.PlayingSound
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.notification.NotificationHelper
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.presentation.model.PlayerPresentation
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.notification.NotificationConstants
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : Service() {

    private val serviceScope = MainScope()

    @Inject
    lateinit var playerRepository: PlayerRepository

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val audioResIdToExoPlayer = mutableMapOf<@receiver:RawRes Int, ExoPlayer>()

    private var playerServiceJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            NotificationConstants.PLAYER_SERVICE_NOTIFICATION_ID,
            notificationHelper.getBasePlayerServiceNotification().build()
        )

        playerServiceJob?.cancel()
        playerServiceJob = serviceScope.launch {
            combineTuple(
                playerRepository.player,
                playerRepository.sounds,
            ).collectLatest { (
                                  player: Player,
                                  sounds: List<Sound>,
                              ) ->
                val soundById = sounds.associateBy { it.id }

                // Remove players of sounds that are no longer playing
                val playingSoundAudioResIds = player.playingSounds
                    .mapNotNull { playingSound: PlayingSound -> soundById[playingSound.id]?.audioResId }
                    .toSet()

                audioResIdToExoPlayer
                    .keys
                    .filter { audioResId: Int -> !playingSoundAudioResIds.contains(audioResId) }
                    .forEach { audioResId: Int -> removeExoPlayer(audioResId = audioResId) }

                // Add a player for each playing sound
                player.playingSounds.forEach { playingSound: PlayingSound ->
                    soundById[playingSound.id]?.audioResId?.let { audioResId: Int ->
                        if (!audioResIdToExoPlayer.containsKey(audioResId)) {
                            Firebase.analytics.logEvent("play_sound_with_exoplayer") {
                                param(FirebaseAnalytics.Param.ITEM_ID, playingSound.id)
                            }
                        }

                        addResPlayer(
                            audioResId = audioResId,
                            volume = playingSound.volume,
                        )
                    }
                }

                when (player.phase) {
                    PlayerPhase.PLAYING -> playAllPlayers()
                    PlayerPhase.PAUSED -> pauseAllPlayers()
                    PlayerPhase.STOPPED -> stopSelf()
                }

                val playerPresentation = PlayerPresentation(
                    phase = player.phase,
                    playingSounds = player.playingSounds.mapNotNull { playingSound: PlayingSound ->
                        when (val sound = soundById[playingSound.id]) {
                            null -> null
                            else -> {
                                SoundPresentation(
                                    id = playingSound.id,
                                    readableName = sound.readableName,
                                    iconResId = sound.iconResId,
                                    audioResId = sound.audioResId,
                                    isPlaying = true,
                                    volume = playingSound.volume,
                                )
                            }
                        }
                    },
                    playingMood = player.playingMood,
                )

                notificationHelper.updatePlayerServiceNotification(playerPresentation)
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAllPlayers()
        serviceScope.cancel()
        notificationHelper.removePlayerServiceNotification()
    }

    private fun stopAllPlayers() {
        audioResIdToExoPlayer.forEach { (audioResId: Int, exoPlayer: ExoPlayer) ->
            with(exoPlayer) {
                pause()
                stop()
                release()
            }
            logcat { "stopped ${audioResId}" }
        }
    }

    private fun pauseAllPlayers() {
        audioResIdToExoPlayer.forEach { (_: Int, exoPlayer: ExoPlayer) -> exoPlayer.pause() }
        logcat { "paused all players" }

    }

    private fun playAllPlayers() {
        audioResIdToExoPlayer.forEach { (_: Int, exoPlayer: ExoPlayer) -> exoPlayer.play() }
        logcat { "playing all players" }
    }

    @OptIn(UnstableApi::class)
    private fun addResPlayer(
        @RawRes audioResId: Int,
        @FloatRange(from = 0.0, to = 1.0) volume: Float,
    ) {
        when {
            !audioResIdToExoPlayer.containsKey(audioResId) -> {
                val uri = RawResourceDataSource.buildRawResourceUri(audioResId)
                val exoPlayer = ExoPlayer.Builder(this)
                    .apply { setHandleAudioBecomingNoisy(true) }
                    .build()
                    .apply {
                        setMediaItem(MediaItem.fromUri(uri))
                        setVolume(volume)
                        repeatMode = ExoPlayer.REPEAT_MODE_ONE
                        prepare()
                    }
                audioResIdToExoPlayer[audioResId] = exoPlayer
                logcat { "added $audioResId player, volume: ${exoPlayer.volume}" }
            }

            audioResIdToExoPlayer[audioResId]?.volume != volume -> {
                audioResIdToExoPlayer[audioResId]?.volume = volume
                logcat { "changed $audioResId volume to $volume" }
            }
        }
    }

    private fun removeExoPlayer(@RawRes audioResId: Int) {
        audioResIdToExoPlayer[audioResId]?.let { exoPlayer: ExoPlayer ->
            exoPlayer.pause()
            exoPlayer.stop()
            exoPlayer.release()
            audioResIdToExoPlayer.remove(audioResId)
            logcat { "removed $audioResId player" }
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null
}