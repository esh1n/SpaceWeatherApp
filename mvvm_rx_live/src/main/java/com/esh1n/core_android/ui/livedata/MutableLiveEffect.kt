package com.esh1n.core_android.ui.livedata

import androidx.lifecycle.LifecycleOwner

class MutableLiveEffect {
    private val liveEvent = MutableLiveEvent<Any>()

    fun happen() {
        liveEvent.value = Any()
    }

    fun postHappen() {
        liveEvent.postValue(Any())
    }

    fun observe(owner: LifecycleOwner, action: () -> Unit) {
        liveEvent.observeNonNull(owner) { action() }
    }
}