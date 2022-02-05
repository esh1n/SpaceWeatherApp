package com.lab.esh1n.weather.data.cache

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface SingleValueCache {

    fun <V : Any> getSource(key: String, type: Class<V>, nullValueReplacement: V): Observable<V>

    fun <V : Any?> getValue(key: String, type: Class<V>): V?

    fun <V : Any> saveValue(key: String, value: V)

    fun deleteValue(key: String)

    fun deleteAll()

}

open class RxPrefs(private val sharedPreferences: SharedPreferences) : SingleValueCache {
    private val gson: Gson = Gson()

    private var publisher = PublishSubject.create<String>()
    private val listener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key -> publisher.onNext(key) }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun <V : Any> getSource(
        key: String,
        type: Class<V>,
        nullValueReplacement: V
    ): Observable<V> {
        fun getValue(key: String, type: Class<V>, nullValueReplacement: V) =
            getValue(key, type) ?: nullValueReplacement
        if (publisher.hasComplete()) {
            publisher = PublishSubject.create()
        }
        return publisher.hide()
            .filter { subjectKey -> subjectKey == key }
            .map { getValue(key, type, nullValueReplacement) }
            .startWith(getValue(key, type, nullValueReplacement))
    }

    override fun <V> getValue(key: String, type: Class<V>): V? {
        val json = sharedPreferences.getString(key, null)
        return json?.let { gson.fromJson(json, type) }
    }

    private fun SharedPreferences.save(action: SharedPreferences.Editor.() -> Unit) =
        Completable.fromAction { edit(true, action) }

    fun <T> getMaybe(supplier: SharedPreferences.() -> T?) = Maybe.fromCallable<T> {
        supplier(sharedPreferences)
    }

    fun <T> get(supplier: SharedPreferences.() -> T) = Single.fromCallable {
        supplier(sharedPreferences)
    }

    private fun <V : Any> SharedPreferences.Editor.putValue(key: String, value: V) {
        putString(key, gson.toJson(value))
    }

    override fun <V : Any> saveValue(key: String, value: V) {
        sharedPreferences.save() {
            putValue(key, value)
        }
    }

    override fun deleteValue(key: String) {
        sharedPreferences.save() {
            remove(key)
        }
    }

    override fun deleteAll() {
        sharedPreferences.save() {
            clear()
        }
    }
}