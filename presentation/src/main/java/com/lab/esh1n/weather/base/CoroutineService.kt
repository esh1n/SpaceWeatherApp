package com.lab.esh1n.weather.base

import android.app.Service

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseCoroutineService : Service(), CoroutineScope {

    private val rootJob = SupervisorJob()

    override val coroutineContext: CoroutineContext = rootJob + Dispatchers.Main

    @CallSuper
    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }

}