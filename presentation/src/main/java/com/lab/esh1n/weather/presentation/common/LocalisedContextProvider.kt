package com.lab.esh1n.weather.presentation.common

import android.content.Context
import com.esh1n.utils_android.ui.ContextUtil
import com.lab.esh1n.weather.domain.ILocalisedContextProvider
import com.lab.esh1n.weather.domain.prefs.IPrefsInteractor
import javax.inject.Inject

class LocalisedContextProvider @Inject constructor(
    private val context: Context,
    private val prefsInteractor: IPrefsInteractor
) : ILocalisedContextProvider {
    override fun getLocalisedContext(): Context =
        ContextUtil.getLocalizedContext(context, prefsInteractor.getLocaleBlocking())
}