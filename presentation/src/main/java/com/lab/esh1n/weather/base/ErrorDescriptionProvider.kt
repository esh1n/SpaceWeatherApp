package com.lab.esh1n.weather.base

import android.content.res.Resources
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.domain.base.ErrorDescriptionProvider
import com.lab.esh1n.weather.domain.base.ErrorModel
import com.lab.esh1n.weather.domain.base.ErrorModel.Kind.*
import java.net.HttpURLConnection

class ErrorDescriptionProviderImpl(private val resources: Resources) : ErrorDescriptionProvider {

    override fun getHumanReadableError(error: ErrorModel): String {
        return resources.getString(getErrorResourceId(error))
    }

    private fun getErrorResourceId(error: ErrorModel): Int {
        return when (error.kind) {
            HTTP -> getHttpErrorResourceId(error.code)
            CONNECTION -> R.string.error_connection_state
            DATABASE -> R.string.error_database
            UNEXPECTED -> R.string.error_unexpected
        }
    }

    private fun getHttpErrorResourceId(code: Int): Int {
        return when (code) {
            HttpURLConnection.HTTP_FORBIDDEN -> R.string.error_access_denied
            HttpURLConnection.HTTP_NOT_FOUND -> R.string.error_not_found
            HttpURLConnection.HTTP_INTERNAL_ERROR -> R.string.error_server
            HttpURLConnection.HTTP_UNAVAILABLE -> R.string.error_http_unavailable
            else -> R.string.error_common
        }
    }
}
