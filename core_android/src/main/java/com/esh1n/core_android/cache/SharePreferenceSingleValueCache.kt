package com.esh1n.core_android.cache

import android.content.SharedPreferences

import com.google.gson.Gson

import io.reactivex.Observable

class SharePreferenceSingleValueCache(private val sharedPreferences: SharedPreferences) :
    SingleValueCache {
    private val gson: Gson = Gson()

    override fun <V : Any> getSource(key: String, type: Class<V>): Observable<SingleValueCache.Result<*>> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun <V : Any?> getValue(key: String, type: Class<V>): V {
        val json = sharedPreferences.getString(key, "")
        return gson.fromJson(json, type)
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
