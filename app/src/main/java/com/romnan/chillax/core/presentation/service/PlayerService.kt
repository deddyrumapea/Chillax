package com.romnan.chillax.core.presentation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.RawRes
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.romnan.chillax.core.domain.model.PlayerPhase
import com.romnan.chillax.core.domain.notification.NotificationHelper
import com.romnan.chillax.core.domain.repository.PlayerStateRepository
import com.romnan.chillax.core.util.Constants
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
    lateinit var playerStateRepository: PlayerStateRepository

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val resPlayers = mutableMapOf<Int, ExoPlayer>()

    private var playerServiceJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            Constants.PLAYER_SERVICE_NOTIFICATION_ID,
            notificationHelper.getBasePlayerServiceNotification().build()
        )

        playerServiceJob?.cancel()
        playerServiceJob = serviceScope.launch {
            playerStateRepository.getState().collectLatest { playerState ->

                // Remove players of sounds that are no longer played
                resPlayers
                    .filter { entry -> !playerState.soundsList.any { it.resource == entry.key } }
                    .forEach { entry -> removeResPlayer(entry.key) }

                // Add a player for each playing sound
                playerState.soundsList.forEach { addResPlayer(resId = it.resource) }

                when (playerState.phase) {
                    PlayerPhase.Playing -> playAllPlayers()
                    PlayerPhase.Paused -> pauseAllPlayers()
                    PlayerPhase.Stopped -> stopSelf()
                }

                notificationHelper.updatePlayerServiceNotification(playerState)
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
        if (resPlayers[resId] == null) { // The sound has not been played yet
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

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val NOTIFICATION_ID = 69420
        const val NOTIFICATION_CHANNEL_ID = "channel_69"
    }
}