package com.romnan.chillax.domain.notification

import androidx.core.app.NotificationCompat
import com.romnan.chillax.domain.model.Player

interface NotificationHelper {
    fun getBasePlayerServiceNotification(): NotificationCompat.Builder

    fun updatePlayerServiceNotification(player: Player)

    fun removePlayerServiceNotification()
}