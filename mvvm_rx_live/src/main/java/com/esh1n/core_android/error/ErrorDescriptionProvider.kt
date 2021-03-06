package com.esh1n.core_android.error

interface ErrorDescriptionProvider {
    fun getHumanReadableError(error: ErrorModel): String
}

interface ErrorTrackerProvider {
    fun trackError(throwable: Throwable)
}
