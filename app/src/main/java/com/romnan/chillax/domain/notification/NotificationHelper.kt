package com.romnan.chillax.domain.notification

import androidx.core.app.NotificationCompat
import com.romnan.chillax.presentation.model.PlayerPresentation

interface NotificationHelper {
    fun getBasePlayerServiceNotification(): NotificationCompat.Builder

    fun updatePlayerServiceNotification(player: PlayerPresentation)

    fun removePlayerServiceNotification()

    fun showBedtimeReminderNotification()
}