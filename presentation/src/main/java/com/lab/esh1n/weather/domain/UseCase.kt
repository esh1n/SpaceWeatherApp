package com.lab.esh1n.weather.domain

import com.esh1n.core_android.error.ErrorsHandler


abstract class UseCase<Result, Arguments>(val errorsHandler: ErrorsHandler) {

    abstract fun perform(args: Arguments): Result
}