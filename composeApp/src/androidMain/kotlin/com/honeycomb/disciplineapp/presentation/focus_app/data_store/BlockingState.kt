package com.honeycomb.disciplineapp.presentation.focus_app.data_store

class BlockingState {
    var isBlocking: Boolean = false; private set
    var remainingSeconds: Long = 0; private set
    private var onUpdate: ((Long) -> Unit)? = null

    fun start(seconds: Long, onTick: (Long) -> Unit) {
        isBlocking = true
        remainingSeconds = seconds
        onUpdate = onTick
        onUpdate?.invoke(seconds)
    }

    fun tick() {
        if (remainingSeconds > 0) {
            remainingSeconds--
            onUpdate?.invoke(remainingSeconds)
        }
    }

    fun finish() {
        isBlocking = false
        remainingSeconds = 0
        onUpdate = null
    }
}

// Singleton for easy access
object BlockerManager {
    val state = BlockingState()
}