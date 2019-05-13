package com.lab.esh1n.weather.domain.base

interface ErrorDescriptionProvider {
    fun getHumanReadableError(error: ErrorModel): String
}