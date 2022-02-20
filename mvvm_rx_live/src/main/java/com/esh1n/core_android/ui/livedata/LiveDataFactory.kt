package com.worka.android.app.common.livedata

import androidx.lifecycle.MutableLiveData
import com.esh1n.core_android.ui.livedata.MutableLiveEffect
import com.esh1n.core_android.ui.livedata.MutableLiveEvent
import com.esh1n.core_android.ui.livedata.SingleObserverMutableLiveEvent

object LiveDataFactory {

    fun <T> mutable(): MutableLiveData<T> = MutableLiveData()

    fun <T> mutable(defaultValue: T): MutableLiveData<T> =
        mutable<T>().apply { value = defaultValue }

    fun <T> singleObserverMutableEvent() = SingleObserverMutableLiveEvent<T>()

    fun <T> mutableEvent() = MutableLiveEvent<T>()

    fun mutableEffect() = MutableLiveEffect()
}