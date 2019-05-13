package com.lab.esh1n.weather.domain.base

class RetrofitException(description: String? = null, val code: Int) : Exception(description) {
}