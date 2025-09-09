package com.romnan.chillax.data.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.romnan.chillax.domain.repository.PlayerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@AndroidEntryPoint
class PlayerActionBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var playerRepository: PlayerRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_PLAY_OR_PAUSE -> {
                CoroutineScope(Dispatchers.IO).launch {
                    playerRepository.playOrPausePlayer()
                }
            }

            else -> {
                logcat { "Unknown action: ${intent?.action}" }
            }
        }
    }

    companion object {
        private const val PENDING_INTENT_REQUEST_CODE = 315
        private const val ACTION_PLAY_OR_PAUSE = "ACTION_PLAY_OR_PAUSE"

        fun getPlayOrPausePendingIntent(appContext: Context): PendingIntent {
            return PendingIntent.getBroadcast(
                appContext,
                PENDING_INTENT_REQUEST_CODE,
                Intent(appContext, PlayerActionBroadcastReceiver::class.java)
                    .apply { action = ACTION_PLAY_OR_PAUSE },
                when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    true -> PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    false -> PendingIntent.FLAG_UPDATE_CURRENT
                },
            )
        }
    }
}