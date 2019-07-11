package com.esh1n.core_android.ui.viewmodel

import androidx.lifecycle.Observer
import com.esh1n.core_android.error.ErrorModel

abstract class BaseObserver<T> : Observer<Resource<T>> {
    override fun onChanged(r: Resource<T>?) {
        if (r != null) {
            when {
                r.status == Resource.Status.SUCCESS -> {
                    onProgress(false)
                    onData(r.data)
                }
                r.status == Resource.Status.LOADING -> {
                    onProgress(true)
                }
                r.status == Resource.Status.ERROR -> {
                    onProgress(false)
                    onError(r.errorModel)
                }
                r.status == Resource.Status.ENDED -> {
                    onProgress(false)
                }
                else -> {
                    onEmptyResourceEmission()
                }
            }
        } else {
            onEmptyResourceEmission()
        }
    }

    abstract fun onData(data: T?)
    open fun onEmptyResourceEmission() {}
    open fun onProgress(progress: Boolean) {}
    abstract fun onError(error: ErrorModel?)
}