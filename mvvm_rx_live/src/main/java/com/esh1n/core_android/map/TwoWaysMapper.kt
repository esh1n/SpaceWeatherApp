package com.esh1n.core_android.map

abstract class TwoWayListMapper<S : Any, T : Any> : ListMapper<S, T>() {


    fun mapInverse(source: List<T>?): List<S> {
        return if (source == null) {
            emptyList()
        } else {
            source.filterNotNull().map { mapInverse(it) }.toMutableList()
        }
    }

    abstract fun mapInverse(source: T): S
}