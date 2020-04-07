package com.lab.esh1n.weather.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.work.WorkManager
import com.esh1n.core_android.error.ErrorDescriptionProvider
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.error.ErrorTrackerProvider
import com.esh1n.core_android.error.ErrorsHandler
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lab.esh1n.weather.R
import dagger.Module
import dagger.Provides
import java.net.HttpURLConnection
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("rch.weather", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideErrorDescriptionProviderImpl(application: Application): ErrorDescriptionProvider {
        return ErrorDescriptionProviderImpl(application.resources)
    }

    @Provides
    @Singleton
    fun provideErrorTrackerImpl(): ErrorTrackerProvider {
        return object : ErrorTrackerProvider {
            override fun trackError(throwable: Throwable) {
                FirebaseCrashlytics.getInstance().recordException(throwable)
            }
        }
    }

    @Provides
    @Singleton
    fun provideErrorHandler(errorDescriptionProvider: ErrorDescriptionProvider, errorTrackerProvider: ErrorTrackerProvider): ErrorsHandler {
        return ErrorsHandler(errorDescriptionProvider, errorTrackerProvider)
    }

    @Provides
    @Singleton
    fun provideWorkManager(application: Application): WorkManager {
        return WorkManager.getInstance(application)
    }
}

class ErrorDescriptionProviderImpl(private val resources: Resources) : ErrorDescriptionProvider {

    override fun getHumanReadableError(error: ErrorModel): String {
        return resources.getString(getErrorResourceId(error))
    }

    private fun getErrorResourceId(error: ErrorModel): Int {
        return when (error.kind) {
            ErrorModel.Kind.HTTP -> getHttpErrorResourceId(error.code)
            ErrorModel.Kind.CONNECTION -> R.string.error_connection_state
            ErrorModel.Kind.DATABASE -> R.string.error_database
            ErrorModel.Kind.UNEXPECTED -> R.string.error_unexpected
        }
    }

    private fun getHttpErrorResourceId(code: Int): Int {
        return when (code) {
            HttpURLConnection.HTTP_FORBIDDEN -> R.string.error_access_denied
            HttpURLConnection.HTTP_NOT_FOUND -> R.string.error_not_found
            HttpURLConnection.HTTP_INTERNAL_ERROR -> R.string.error_server
            HttpURLConnection.HTTP_UNAVAILABLE -> R.string.error_http_unavailable
            else -> R.string.error_common
        }
    }
}
