package com.esh1n.core_android.error

import android.content.res.Resources
import com.esh1n.core_android.R
import com.esh1n.core_android.retrofit.RetrofitException
import retrofit2.Response
import java.net.HttpURLConnection.*

class ErrorsHandler(private val mResources: Resources) {

    @JvmOverloads
    fun handle(error: Throwable, login: Boolean = false): String {

        var message = mResources.getString(R.string.error_common)

        if (error is RetrofitException) {
            val response = error.response
            if (response == null) {
                message = error.message ?: mResources.getString(R.string.error_connection_state)
                //.mResources.getString(R.string.error_connection_state)
            } else {
                message = mResources.getString(getMessageResource(response, login))
            }
        }
        if (error is IllegalStateException) {
            message = error.message
        }

        return message
    }
    private fun getMessageResource(response: Response<*>, login: Boolean): Int {
        when (response.code()) {
            HTTP_BAD_REQUEST -> return if (login) R.string.error_invalid_login else R.string.error_server
            HTTP_UNAUTHORIZED -> return R.string.error_unauth
            HTTP_FORBIDDEN -> return R.string.error_access_denied
            HTTP_NOT_FOUND -> return R.string.error_server
            HTTP_INTERNAL_ERROR -> return R.string.error_server
            HTTP_UNAVAILABLE -> return R.string.error_server
            else -> return R.string.error_common
        }
    }
}