package com.romnan.chillax.presentation.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.romnan.chillax.R
import com.romnan.chillax.domain.notification.NotificationHelper
import com.romnan.chillax.presentation.MainActivity
import com.romnan.chillax.presentation.model.PlayerPresentation
import com.romnan.chillax.presentation.notification.NotificationConstants.BEDTIME_NOTIFICATION_ID
import com.romnan.chillax.presentation.notification.NotificationConstants.BEDTIME_REMINDER_CHANNEL_ID
import com.romnan.chillax.presentation.notification.NotificationConstants.PLAYER_SERVICE_CHANNEL_ID
import com.romnan.chillax.presentation.notification.NotificationConstants.PLAYER_SERVICE_NOTIFICATION_ID
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

    private val openMainActivityIntent = Intent(appContext, MainActivity::class.java)

    private val openMainActivityPendingIntent = PendingIntent.getActivity(
        appContext, 0, openMainActivityIntent, pendingIntentFlags
    )

    init {
        createNotificationChannels()
    }

    override fun getBasePlayerServiceNotification(): NotificationCompat.Builder =
        NotificationCompat.Builder(appContext, PLAYER_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dark_mode_fill)
            .setContentIntent(openMainActivityPendingIntent)
            .setSilent(true)
            .setOnlyAlertOnce(true)

    override fun updatePlayerServiceNotification(player: PlayerPresentation) {
        val updatedNotification = getBasePlayerServiceNotification()
            .setContentTitle(player.contentTitle.asString(appContext))
            .setContentText(player.soundsTitle.asString(appContext))
            .build()

        notificationManager.notify(PLAYER_SERVICE_NOTIFICATION_ID, updatedNotification)
    }

    override fun removePlayerServiceNotification() {
        notificationManager.cancel(PLAYER_SERVICE_NOTIFICATION_ID)
    }

    override fun showBedtimeReminderNotification() {
        val notification = NotificationCompat.Builder(appContext, BEDTIME_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dark_mode_fill)
            .setContentIntent(openMainActivityPendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(appContext.getString(R.string.bed_time_content_title))
            .setContentText(appContext.getString(R.string.bed_time_content_text))
            .setAutoCancel(true)
            .build()

        notificationManager.notify(BEDTIME_NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannels() {
        val channels = listOf(
            NotificationChannelCompat.Builder(
                PLAYER_SERVICE_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_DEFAULT
            )
                .setName(appContext.getString(R.string.player_service_notif_channel_name))
                .setDescription(appContext.getString(R.string.player_service_notif_channel_desc))
                .setSound(null, null)
                .build(),

            NotificationChannelCompat.Builder(
                BEDTIME_REMINDER_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH
            )
                .setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                )
                .setName(appContext.getString(R.string.bed_time_notifications))
                .setDescription(appContext.getString(R.string.bed_time_notif_channel_desc))
                .setLightsEnabled(true)
                .setVibrationEnabled(true)
                .build(),
        )

        notificationManager.createNotificationChannelsCompat(channels)
    }
}