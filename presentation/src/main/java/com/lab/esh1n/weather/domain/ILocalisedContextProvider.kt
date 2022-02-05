package com.lab.esh1n.weather.domain

import android.content.Context

interface ILocalisedContextProvider {
    fun getLocalisedContext(): Context
}