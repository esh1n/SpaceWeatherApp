package com.esh1n.core_android.error

import com.esh1n.core_android.retrofit.RetrofitException
import java.net.HttpURLConnection

class ErrorsHandler(private val errorDescriptionProvider: ErrorDescriptionProvider, private val errorTrackerProvider: ErrorTrackerProvider) {

    fun handle(throwable: Throwable): ErrorModel {
        val errorModel = convertToErrorModel(throwable)
        if (errorModel.message.isBlank()) {
            errorModel.message = errorDescriptionProvider.getHumanReadableError(errorModel)
        }
        return errorModel
    }

    private fun convertToErrorModel(throwable: Throwable): ErrorModel {
        errorTrackerProvider.trackError(throwable)
        if (throwable is RetrofitException) {
            val response = throwable.message
            return if (response == null) {
                ErrorModel.connectionError()
            } else {
                ErrorModel.httpError(code = throwable.response?.code()
                        ?: HttpURLConnection.HTTP_INTERNAL_ERROR)
            }
        }
        return ErrorModel.unexpectedError(throwable.localizedMessage)
    }

}