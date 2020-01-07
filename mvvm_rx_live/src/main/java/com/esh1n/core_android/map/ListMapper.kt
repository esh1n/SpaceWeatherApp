package com.esh1n.core_android.map

abstract class ListMapper<S : Any, T : Any> : Mapper<S, T>() {

    fun map(source: List<S?>?): List<T> {
        return if (source == null) {
            emptyList()
        } else {
            source.filterNotNull().map { map(it) }.toList()
        }
    }

}

abstract class Mapper<S : Any, T : Any> {

    abstract fun map(source: S): T

}