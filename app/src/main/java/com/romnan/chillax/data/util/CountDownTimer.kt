package com.romnan.chillax.data.util

import com.romnan.chillax.di.ApplicationScope
import com.romnan.chillax.domain.util.TimeSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountDownTimer @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    private val timeSource: TimeSource,
) {
    private var millisUntilFinished = 0L

    private var timerJob: Job? = null

    fun startTimer(
        durationMillis: Long,
        countDownInterval: Long,
        onTick: suspend (millisUntilFinished: Long) -> Unit,
        onFinish: () -> Unit,
    ) {
        millisUntilFinished = durationMillis
        val startTime = timeSource.elapsedRealTime
        val targetTime = startTime + durationMillis
        timerJob = appScope.launch {
            while (true) {
                if (timeSource.elapsedRealTime < targetTime) {
                    delay(minOf(countDownInterval, durationMillis))
                    millisUntilFinished = targetTime - timeSource.elapsedRealTime
                    onTick(millisUntilFinished)
                } else {
                    onFinish()
                    break
                }
            }
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }
}