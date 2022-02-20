package com.esh1n.core_android.common

import retrofit2.HttpException
import java.net.HttpURLConnection

sealed class Error(open val e: Throwable) : Exception()

data class ApiError(val description: String?, override val e: Throwable) : Error(e)
data class NetworkError(override val e: Throwable) : Error(e)
data class GeneralError(override val e: Throwable) : Error(e) {
    companion object {
        val EMPTY = GeneralError(RuntimeException())
    }
}

fun ApiError.is400(): Boolean {
    return e is HttpException && e.code() == HttpURLConnection.HTTP_BAD_REQUEST
}

fun ApiError.is403(): Boolean {
    return e is HttpException && e.code() == HttpURLConnection.HTTP_FORBIDDEN
}

fun ApiError.is404(): Boolean {
    return e is HttpException && e.code() == HttpURLConnection.HTTP_NOT_FOUND
}

fun ApiError.is409(): Boolean {
    return e is HttpException && e.code() == HttpURLConnection.HTTP_CONFLICT
}

fun ApiError.isUnauthorized(): Boolean {
    return e is HttpException && e.code() == HttpURLConnection.HTTP_UNAUTHORIZED
}

fun ApiError.body() = (e as? HttpException)?.response()?.errorBody()?.string()

