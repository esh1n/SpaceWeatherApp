package com.esh1n.core_android.map

abstract class TwoWayMapper<S : Any, T : Any> : Mapper<S, T>() {


    fun mapInverse(source: List<T>?): List<S> {
        return if (source == null) {
            emptyList()
        } else {
            source.filterNotNull().map { mapInverse(it) }.toMutableList()
        }
    }

    abstract fun mapInverse(source: T): S
}