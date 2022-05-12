package com.romnan.chillax.core

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.RawRes
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.romnan.chillax.core.domain.model.PlayableSound
import logcat.logcat

class PlayerService : Service() {
    private val resPlayers = mutableMapOf<Int, ExoPlayer>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent
            ?.getParcelableArrayListExtra<PlayableSound>(EXTRA_PLAYABLE_SOUND_ARRAYLIST)
            ?.let { playableSounds ->
                playableSounds
                    .filter { it.isPlaying }
                    .forEach { addSoundPlayer(it.resId) }

                playableSounds
                    .filter { !it.isPlaying }
                    .forEach { removeSoundPlayer(it.resId) }
            }
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        // TODO: make sure to clear all sound players in onDestroy
//        soundPlayers.forEach {
//            // Stop all ExoPlayers
//            with(it.value) {
//                playWhenReady = false
//                stop()
//                release()
//            }
//        }
        logcat { "service destroyed" }
        super.onDestroy()
    }

    private fun addSoundPlayer(@RawRes resId: Int) {
        if (resPlayers[resId] == null) { // The sound has not been played yet
            val player = ExoPlayer.Builder(this)
                .apply { setHandleAudioBecomingNoisy(true) }
                .build()
                .apply {
                    val uri = RawResourceDataSource.buildRawResourceUri(resId)
                    setMediaItem(MediaItem.fromUri(uri))
                    repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    prepare()
                    play()
                }
            resPlayers[resId] = player
            logcat { "PLAYING $resId" }
        }
    }

    private fun removeSoundPlayer(@RawRes resId: Int) {
        resPlayers[resId]?.let {
            if (it.isPlaying) { // The sound is currently playing
                it.playWhenReady = false
                it.stop()
                it.release()
                resPlayers.remove(resId)
                logcat { "STOP $resId" }
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val EXTRA_PLAYABLE_SOUND_ARRAYLIST = "EXTRA_PLAYABLE_SOUND_ARRAYLIST"
    }
}