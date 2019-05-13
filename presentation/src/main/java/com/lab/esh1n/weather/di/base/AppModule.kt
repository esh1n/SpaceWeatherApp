package com.lab.esh1n.weather.di.base

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.lab.esh1n.weather.base.ErrorDescriptionProviderImpl
import com.lab.esh1n.weather.domain.base.ErrorDescriptionProvider
import com.lab.esh1n.weather.domain.base.ErrorsHandler
import dagger.Module
import dagger.Provides
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
    internal fun provideErrorDescriptionProvider(application: Application): ErrorDescriptionProvider {
        return ErrorDescriptionProviderImpl(application.resources)
    }


    @Provides
    @Singleton
    fun provideErrorHandler(errorDescriptionProvider: ErrorDescriptionProvider): ErrorsHandler {
        return ErrorsHandler(errorDescriptionProvider)
    }

    @Provides
    @Singleton
    fun provideWorkManager(): WorkManager {
        return WorkManager.getInstance()
    }
}