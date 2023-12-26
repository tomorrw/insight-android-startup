package com.tomorrow.lda.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class Timer(
    val action: (CoroutineScope) -> Unit = {},
    val timeout: Int = 0,
    val timeoutMillis: Long = 1000,
) {
    private var coroutineScope: CoroutineScope = CoroutineScope(context = EmptyCoroutineContext)
    private var taskJob: Job? = null
    private var timeLeft = timeout

    fun setTimeout(
        timeoutMillis: Long = 500,
        action: () -> Unit
    ) {
        taskJob = coroutineScope.launch(Dispatchers.Main) {
            if (isActive) {
                delay(timeoutMillis)
                action()
                stop()
            }
        }
    }

    fun loop() {
        taskJob = coroutineScope.launch(Dispatchers.Main) {
            if (isActive) {
                action(this@launch)
                delay(timeoutMillis)
                loop()
            }
        }
    }

    fun start() {
        taskJob = coroutineScope.launch(Dispatchers.Main) {
            if (isActive) {
                if (timeLeft == 0) {
                    action(this@launch)
                    reset()
                    return@launch
                }
                timeLeft -= 1
                delay(timeoutMillis)
                start()
            }
        }
    }

    fun reset() {
        this.stop()
        this.timeLeft = timeout
    }

    fun restart() {
        this.reset()
        this.start()
    }

    fun stop() = taskJob?.cancel()
}