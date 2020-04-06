package com.esh1n.core_android.ui.viewmodel

import androidx.lifecycle.Observer
import com.esh1n.core_android.error.ErrorModel

abstract class BaseObserver<T> : Observer<Resource<T>> {
    override fun onChanged(r: Resource<T>?) {
        if (r != null) {
            when (r.status) {
                Resource.Status.SUCCESS -> {
                    onProgress(false)
                    onData(r.data)
                }
                Resource.Status.LOADING -> {
                    onProgress(true)
                }
                Resource.Status.ERROR -> {
                    onProgress(false)
                    onError(r.errorModel)
                }
                Resource.Status.COMPLETED -> {
                    onProgress(false)
                    onCompleted()
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
    open fun onCompleted() {}
    open fun onProgress(progress: Boolean) {}
    abstract fun onError(error: ErrorModel?)
}