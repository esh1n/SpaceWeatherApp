package com.esh1n.core_android.cache

import android.support.annotation.NonNull
import com.esh1n.core_android.cache.CacheContext.SingleValueCachePolicy.FILE_SYSTEM_SECURE
import com.esh1n.core_android.cache.CacheContext.SingleValueCachePolicy.MAIN_STORAGE
import java.util.*

open class CacheContext(fileSystemSingleValueCache: SingleValueCache) {
    protected val mSingleValueCaches = HashMap<SingleValueCachePolicy, SingleValueCache>()

    init {
        mSingleValueCaches[MAIN_STORAGE] = RAMSingleValueCache()
        mSingleValueCaches[FILE_SYSTEM_SECURE] = fileSystemSingleValueCache
    }

    fun clearSession() {
        for (policy in SingleValueCachePolicy.values()) {
            mSingleValueCaches[policy]?.deleteAll()
        }
    }

    @NonNull
    fun getSingleValueCache(policy: SingleValueCachePolicy): SingleValueCache {
        return mSingleValueCaches[policy]
                ?: throw IllegalArgumentException("Single Value Cache doesn't exist:$policy")
    }

    fun prefsSecureStorage():SingleValueCache?{
       return mSingleValueCaches[SingleValueCachePolicy.FILE_SYSTEM_SECURE]
    }

    fun <E : Any> saveEntityToSVC(@NonNull entity: E, policy: SingleValueCachePolicy) {
        val mainStorage = mSingleValueCaches[policy]
        mainStorage?.saveValue(entity.javaClass.getSimpleName(), entity)
    }

    fun <E> removeEntityFromSVC(@NonNull entityClass: Class<E>, policy: SingleValueCachePolicy) {
        val mainStorage = mSingleValueCaches[policy]
        mainStorage?.deleteValue(entityClass.simpleName)
    }

    fun <E : Any> getEntityFromSVC(entityClass: Class<E>, policy: SingleValueCachePolicy): E? {
        val mainStorage = mSingleValueCaches[policy]
        return mainStorage?.getValue(entityClass.simpleName, entityClass)
    }

    fun <E : Any> saveEntityToRAMSVC(@NonNull entity: E) {
        saveEntityToSVC(entity, SingleValueCachePolicy.MAIN_STORAGE)
    }

    fun <E> removeEntityFromRAMSVC(@NonNull entityClass: Class<E>) {
        removeEntityFromSVC(entityClass, SingleValueCachePolicy.MAIN_STORAGE)
    }

    fun <E : Any> getEntityFromRAMSVC(entityClass: Class<E>): E? {
        return getEntityFromSVC(entityClass, SingleValueCachePolicy.MAIN_STORAGE)
    }

    enum class SingleValueCachePolicy {
        MAIN_STORAGE, FILE_SYSTEM_SECURE
    }
}
