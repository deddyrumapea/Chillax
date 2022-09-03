package com.romnan.chillax.data.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.romnan.chillax.domain.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import logcat.logcat
import javax.inject.Inject

@AndroidEntryPoint
class BedtimeBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(p0: Context?, p1: Intent?) {
        logcat { "bedtime broadcast received" }
        notificationHelper.showBedtimeReminderNotification()
    }

    companion object {
        private const val PENDING_INTENT_REQUEST_CODE = 314

        fun getPendingIntent(appContext: Context): PendingIntent {
            return PendingIntent.getBroadcast(
                appContext,
                PENDING_INTENT_REQUEST_CODE,
                Intent(appContext, BedtimeBroadcastReceiver::class.java),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }
}