package com.lab.esh1n.weather.domain.base

abstract class Mapper<S, T> {

    fun map(source: List<S>?): List<T> {
        return if (source == null) {
            emptyList()
        } else {
            source.filter { it != null }.map { map(it) }.toMutableList()
        }
    }

    abstract fun map(source: S): T

}