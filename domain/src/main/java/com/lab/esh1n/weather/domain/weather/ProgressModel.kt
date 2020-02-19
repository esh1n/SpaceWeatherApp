package com.lab.esh1n.weather.domain.weather

class ProgressModel<DATA>(val progress: Int, val description: String, val data: DATA? = null) {

    val isDone: Boolean
        get() = progress == 100

}