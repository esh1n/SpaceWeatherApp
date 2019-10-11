package com.lab.esh1n.data.cache

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface SingleValueCache {

    fun <V : Any> getSource(key: String, type: Class<V>, fallback: V): Observable<V>

    fun <V : Any?> getValue(key: String, type: Class<V>, fallback: V): V?

    fun <V : Any> saveValue(key: String, value: V)

    fun deleteValue(key: String)

    fun deleteAll()

}

open class SettingsInteractor(private val sharedPreferences: SharedPreferences) : SingleValueCache {
    private val gson: Gson = Gson()

    private var publisher = PublishSubject.create<String>()
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> publisher.onNext(key) }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun <V : Any> getSource(key: String, type: Class<V>, fallback: V): Observable<V> {
        if (publisher.hasComplete()) {
            publisher = createSubject()
        }
        return publisher.hide()
                .filter { subjectKey -> subjectKey == key }
                .map { getValue(key, type, fallback) }
                .startWith(getValue(key, type, fallback))
    }

    fun getStringSource(key: String, fallback: String): Observable<String> {
        return getSource(key, String::class.java, fallback)
    }

    private fun createSubject(): PublishSubject<String> {
        return PublishSubject.create()
    }

    override fun <V : Any?> getValue(key: String, type: Class<V>, fallback: V): V {
        if (type == String::class.java) {
            return sharedPreferences.getString(key, fallback.toString()) as V
        }
        if (type == Boolean::class.java) {
            return sharedPreferences.getBoolean(key, fallback as Boolean) as V
        }
        if (type == Int::class.java) {
            return sharedPreferences.getInt(key, fallback as Int) as V
        }
        val json = sharedPreferences.getString(key, "")
        return gson.fromJson(json, type)
    }

    fun getString(key: String, fallback: String): String {
        return getValue(key, String::class.java, fallback)
    }

    override fun <V : Any> saveValue(key: String, value: V) {
        val json = gson.toJson(value)
        sharedPreferences.edit().putString(key, json).apply()
    }

    override fun deleteValue(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun deleteAll() {
        sharedPreferences.edit().clear().apply()
    }
}