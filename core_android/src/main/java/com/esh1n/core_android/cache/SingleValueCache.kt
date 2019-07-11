package com.esh1n.core_android.cache

import io.reactivex.Observable

interface SingleValueCache {

    fun <V : Any> getSource(key: String, type: Class<V>): Observable<Result<*>>

    fun <V : Any?> getValue(key: String, type: Class<V>): V?

    fun <V : Any> saveValue(key: String, value: V)

    fun deleteValue(key: String)

    fun deleteAll()

    class Result<T : Any>(val res: Any) {

        @Suppress("UNCHECKED_CAST")
        fun getValue(): T {
            return res as T
        }
    }

    companion object {

        const val KEY_TOKEN = "TOKEN"
        const val KEY_SYNC_TIMESTAMP = "KEY_SYNC_TIMESTAMP"
        const val USER_EMAIL = "USER_EMAIL"
    }
}
