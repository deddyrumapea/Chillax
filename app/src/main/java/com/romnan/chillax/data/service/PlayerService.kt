package com.romnan.chillax.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.RawRes
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.romnan.chillax.data.notification.NotificationHelperImpl.Companion.PLAYER_SERVICE_NOTIFICATION_ID
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.notification.NotificationHelper
import com.romnan.chillax.domain.repository.PlayerRepository
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

    private val resPlayers = mutableMapOf<Int, ExoPlayer>()

    private var playerServiceJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            PLAYER_SERVICE_NOTIFICATION_ID,
            notificationHelper.getBasePlayerServiceNotification().build()
        )

        playerServiceJob?.cancel()
        playerServiceJob = serviceScope.launch {
            playerRepository.player.collectLatest { player ->

                // Remove players of sounds that are no longer played
                resPlayers
                    .filter { entry -> !player.sounds.any { it.audioResId == entry.key } }
                    .forEach { entry -> removeResPlayer(entry.key) }

                // Add a player for each playing sound
                player.sounds.forEach { sound ->
                    when {
                        !resPlayers.contains(sound.audioResId) ->
                            addResPlayer(resId = sound.audioResId)

                        resPlayers[sound.audioResId]?.volume != sound.volume ->
                            changeVolume(resId = sound.audioResId, volume = sound.volume)
                    }
                }

                when (player.phase) {
                    PlayerPhase.PLAYING -> playAllPlayers()
                    PlayerPhase.PAUSED -> pauseAllPlayers()
                    PlayerPhase.STOPPED -> stopSelf()
                }

                notificationHelper.updatePlayerServiceNotification(player)
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
        resPlayers.forEach {
            with(it.value) {
                pause()
                stop()
                release()
            }
            logcat { "stopped ${it.key}" }
        }
    }

    private fun pauseAllPlayers() {
        resPlayers.forEach { it.value.pause() }
        logcat { "paused all players" }

    }

    private fun playAllPlayers() {
        resPlayers.forEach { it.value.play() }
        logcat { "playing all players" }
    }

    private fun addResPlayer(@RawRes resId: Int) {
        val player = ExoPlayer.Builder(this)
            .apply { setHandleAudioBecomingNoisy(true) }
            .build()
            .apply {
                val uri = RawResourceDataSource.buildRawResourceUri(resId)
                setMediaItem(MediaItem.fromUri(uri))
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
                prepare()
            }
        resPlayers[resId] = player
        logcat { "added $resId player" }
    }

    private fun removeResPlayer(@RawRes resId: Int) {
        resPlayers[resId]?.let {
            it.pause()
            it.stop()
            it.release()
            resPlayers.remove(resId)
            logcat { "removed $resId player" }
        }
    }

    private fun changeVolume(resId: Int, volume: Float) {
        resPlayers[resId]?.volume = volume
        logcat { "changed $resId volume to $volume" }
    }

    override fun onBind(p0: Intent?): IBinder? = null
}