package com.romnan.chillax.data.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.PlayerState
import com.romnan.chillax.domain.notification.NotificationHelper
import com.romnan.chillax.presentation.MainActivity
import com.romnan.chillax.presentation.util.asString

class NotificationHelperImpl(
    private val appContext: Context
) : NotificationHelper {

    private val notificationManager = NotificationManagerCompat.from(appContext)

    private val pendingIntentFlags =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

    // TODO: fix intent opening duplicate MainActivity
    private val openMainActivityIntent = Intent(appContext, MainActivity::class.java)

    private val openMainActivityPendingIntent = PendingIntent.getActivity(
        appContext, 0, openMainActivityIntent, pendingIntentFlags
    )

    init {
        createNotificationChannels()
    }

    override fun getBasePlayerServiceNotification(): NotificationCompat.Builder =
        NotificationCompat.Builder(appContext, PLAYER_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_music_note_24)
            .setContentIntent(openMainActivityPendingIntent)
            .setSilent(true)
            .setOnlyAlertOnce(true)

    override fun updatePlayerServiceNotification(playerState: PlayerState) {
        // TODO: extract string format
        val contentTitle: String = playerState.playingSounds.let {
            when {
                it.size == 0 -> appContext.getString(R.string.no_sound_is_playing)
                it.size == 1 -> it[0].readableName.asString(context = appContext)
                it.size == 2 -> "${it[0].readableName.asString(context = appContext)} and ${
                    it[1].readableName.asString(context = appContext)
                }"
                it.size > 2 -> "${it[0].readableName.asString(context = appContext)}, ${
                    it[1].readableName.asString(context = appContext)
                }, and other sounds"
                else -> ""
            }
        }

        val contentText = when (playerState.phase) {
            PlayerPhase.PLAYING -> "Playing ${playerState.playingSounds.size} sound(s)"
            PlayerPhase.PAUSED -> "Paused ${playerState.playingSounds.size} sound(s)"
            PlayerPhase.STOPPED -> "Stopped playing sounds"
        }

        // TODO: add notif action
        val updatedNotification = getBasePlayerServiceNotification()
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .build()

        notificationManager.notify(PLAYER_SERVICE_NOTIFICATION_ID, updatedNotification)
    }

    override fun removePlayerServiceNotification() {
        notificationManager.cancel(PLAYER_SERVICE_NOTIFICATION_ID)
    }

    private fun createNotificationChannels() {
        val playerServiceChannel = NotificationChannelCompat.Builder(
            PLAYER_SERVICE_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(appContext.getString(R.string.player_service_notif_channel_name))
            .setDescription(appContext.getString(R.string.player_service_notif_channel_desc))
            .setSound(null, null)
            .build()

        notificationManager.createNotificationChannelsCompat(
            listOf(playerServiceChannel)
        )
    }

    companion object {
        const val PLAYER_SERVICE_CHANNEL_ID = "PLAYER_SERVICE_CHANNEL_ID"
        const val PLAYER_SERVICE_NOTIFICATION_ID = 69
    }
}