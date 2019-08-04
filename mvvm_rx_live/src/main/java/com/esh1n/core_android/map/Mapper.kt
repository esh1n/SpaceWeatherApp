package com.esh1n.core_android.map

abstract class Mapper<S : Any, T : Any> {

    fun map(source: List<S?>?): List<T> {
        return if (source == null) {
            emptyList()
        } else {
            source.filterNotNull().map { map(it) }.toList()
        }
    }

    abstract fun map(source: S): T

}