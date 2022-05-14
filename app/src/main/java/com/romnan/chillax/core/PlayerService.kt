package com.romnan.chillax.core

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.RawRes
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.romnan.chillax.R
import com.romnan.chillax.core.domain.repository.PlayerStateRepository
import com.romnan.chillax.core.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : Service() {

    private val serviceScope = MainScope()

    @Inject
    lateinit var playerStateRepository: PlayerStateRepository

    private val resPlayers = mutableMapOf<Int, ExoPlayer>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO: check api level before implementing this notification
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val channel = NotificationChannelCompat.Builder(
            NOTIFICATION_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(NOTIFICATION_CHANNEL_ID)
            .setDescription(NOTIFICATION_CHANNEL_ID)
            .setSound(null, null)
            .build()

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.createNotificationChannel(channel)

        startForeground(
            NOTIFICATION_ID,
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getString(R.string.notification_content))
                .setSmallIcon(R.drawable.ic_baseline_airline_seat_recline_normal_24)
                .setContentIntent(pendingIntent)
                .build()
        )

        serviceScope.launch {
            playerStateRepository.getState().collectLatest { playerState ->

                // Remove players of sounds that are no longer played
                resPlayers
                    .filter { entry -> !playerState.soundsList.any { it.resource == entry.key } }
                    .forEach { entry -> removeResPlayer(entry.key) }

                // Add a player for each playing sound
                playerState.soundsList.forEach { addResPlayer(resId = it.resource) }

                if (playerState.isPlaying) playAllPlayers() else pauseAllPlayers()
            }
        }

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        resPlayers.forEach {
            // Stop ExoPlayer
            with(it.value) {
                pause()
                stop()
                release()
            }
            logcat { "onDestroy: stopped ${it.key}" }
        }
        logcat { "service destroyed" }
        super.onDestroy()
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