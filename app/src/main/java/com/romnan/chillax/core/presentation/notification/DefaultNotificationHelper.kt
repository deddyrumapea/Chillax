package com.romnan.chillax.core.presentation.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.romnan.chillax.R
import com.romnan.chillax.core.domain.model.PlayerPhase
import com.romnan.chillax.core.domain.model.PlayerState
import com.romnan.chillax.core.domain.notification.NotificationHelper
import com.romnan.chillax.core.presentation.MainActivity
import com.romnan.chillax.core.util.Constants.PLAYER_SERVICE_CHANNEL_ID
import com.romnan.chillax.core.util.Constants.PLAYER_SERVICE_NOTIFICATION_ID

class DefaultNotificationHelper(
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
        val contentTitle: String = playerState.soundsList.let {
            when {
                it.size == 0 -> appContext.getString(R.string.no_sound_is_playing)
                it.size == 1 -> appContext.getString(it[0].name)
                it.size == 2 -> "${appContext.getString(it[0].name)} and ${appContext.getString(it[1].name)}"
                it.size > 2 -> "${appContext.getString(it[0].name)}, ${appContext.getString(it[1].name)}, and other sounds"
                else -> ""
            }
        }

        val contentText = when (playerState.phase) {
            PlayerPhase.Playing -> "Playing ${playerState.soundsList.size} sound(s)"
            PlayerPhase.Paused -> "Paused ${playerState.soundsList.size} sound(s)"
            PlayerPhase.Stopped -> "Stopped playing sounds"
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
}