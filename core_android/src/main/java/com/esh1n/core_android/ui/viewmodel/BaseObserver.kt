package com.esh1n.core_android.ui.viewmodel

import android.arch.lifecycle.Observer

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
                    onError(r.message)
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
    abstract fun onError(message: String)
}