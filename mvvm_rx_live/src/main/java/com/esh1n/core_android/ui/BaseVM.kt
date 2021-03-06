package com.esh1n.core_android.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseVM() : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
    }

    protected fun Disposable.disposeOnDestroy() {
        disposables.add(this)
    }
}