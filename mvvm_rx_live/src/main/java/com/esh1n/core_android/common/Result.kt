package com.esh1n.core_android.common

sealed class Result<T> {
    inline fun onSuccess(dataChanged: (T) -> Unit): Result<T> {
        if (this is Success<T>) {
            dataChanged(this.data)
        }
        return this
    }

    inline fun onFailure(failure: (Error) -> Unit): Result<T> {
        if (this is Failure<T>) {
            failure(this.error)
        }
        return this
    }

    inline fun onLoading(loading: () -> Unit): Result<T> {
        if (this is Loading<T>) {
            loading()
        }
        return this
    }

    inline fun onComplete(action: () -> Unit): Result<T> {
        if (this is Failure<T> || this is Success<T>) {
            action()
        }
        return this
    }

    open val data: T? = null
}

data class Success<T>(override val data: T) : Result<T>()

data class Failure<T>(val error: Error) : Result<T>()

class Loading<T> : Result<T>()

inline fun <T, R> Result<T>.map(f: (T) -> R): Result<R> =
    flatMap { value -> Success(f(value)) }

inline fun <T, R> Result<T>.flatMap(f: (T) -> Result<R>): Result<R> =
    when (this) {
        is Success<T> -> f(data)
        is Failure -> Failure(error)
        is Loading -> Loading()
    }
