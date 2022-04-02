package com.lab.esh1n.weather.weather

import android.util.Log
import com.esh1n.core_android.common.subscribeOnError
import com.esh1n.core_android.ui.viewmodel.AutoClearViewModel
import com.lab.esh1n.weather.domain.prefs.IPrefsInteractor
import com.worka.android.app.common.livedata.LiveDataFactory
import javax.inject.Inject

class LanguageChangeVM @Inject constructor(private val prefsInteractor: IPrefsInteractor) :
    AutoClearViewModel() {
    val langChangeEffect = LiveDataFactory.mutableEffect()

    init {
        subscribeToLanguageUpdates()
    }

    private fun subscribeToLanguageUpdates() {
        prefsInteractor
            .getUserSelectedLanguageUpdates()
            .subscribeOnError(
                { langChangeEffect.happen() },
                { error -> Log.d(LanguageChangeVM::class.java.toString(), error.message ?: "") })
            .disposeOnDestroy()
    }
}