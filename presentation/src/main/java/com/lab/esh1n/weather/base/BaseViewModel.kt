package com.lab.esh1n.weather.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel() : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
    }

    fun addDisposable(d: Disposable) {
        disposables.add(d)
    }
}