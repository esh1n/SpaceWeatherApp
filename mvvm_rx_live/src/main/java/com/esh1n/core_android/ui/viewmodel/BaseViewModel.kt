package com.esh1n.core_android.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
    }

    fun addDisposable(d: Disposable) {
        disposables.add(d)
    }
}