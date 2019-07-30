package com.esh1n.utils_android.threading

import java.util.concurrent.Executors

object ExecutorsUtil {
    private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

    /**
     * Utility method to run blocks on a dedicated background thread, used for io/database work.
     */
    fun ioThread(f: () -> Unit) {
        IO_EXECUTOR.execute(f)
    }
}