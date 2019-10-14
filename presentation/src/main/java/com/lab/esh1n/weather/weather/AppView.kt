package com.lab.esh1n.weather.weather

import android.content.Context

interface AppView {
    fun applySelectedAppLanguage(context: Context): Context
}