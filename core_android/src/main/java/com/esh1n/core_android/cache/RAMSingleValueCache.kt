package com.esh1n.core_android.cache

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.*

class RAMSingleValueCache : SingleValueCache {
    override fun <V : Any> getSource(key: String, type: Class<V>): Observable<SingleValueCache.Result<*>> {
        if (updateSubject.hasComplete()) {
            updateSubject = createSubject()
        }

        updateSubject.onNext(SingleValueCache.Result<Any>(getValue(key, type)))

        return updateSubject.hide()
                .filter { result -> type.isInstance(result.res) }
    }

    override fun <V : Any> saveValue(key: String, value: V) {
        mSingleValueCache[key] = value

        if (updateSubject.hasObservers()) {
            updateSubject.onNext(
                SingleValueCache.Result<Any>(getValue(key, value.javaClass))
            )
        }
    }

    private var updateSubject: Subject<SingleValueCache.Result<*>> = createSubject()
    private val mSingleValueCache = HashMap<String, Any>()

    private fun createSubject(): BehaviorSubject<SingleValueCache.Result<*>> {
        return BehaviorSubject.create()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <V : Any?> getValue(key: String, type: Class<V>): V {
        return mSingleValueCache[key] as V
    }


    override fun deleteValue(key: String) {
        mSingleValueCache.remove(key)
    }

    override fun deleteAll() {
        mSingleValueCache.clear()
    }
}
