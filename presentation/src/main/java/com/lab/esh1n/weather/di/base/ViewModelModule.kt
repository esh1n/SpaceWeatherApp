package com.lab.esh1n.weather.di.base

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lab.esh1n.weather.di.ViewModelKey
import com.lab.esh1n.weather.di.weather.EventsModule
import com.lab.esh1n.weather.weather.viewmodel.EventDetailViewModel
import com.lab.esh1n.weather.weather.viewmodel.EventsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by esh1n on 3/9/18.
 */
@Module(includes = [EventsModule::class])
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    abstract fun provideEventsViewModel(eventsViewModel: EventsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventDetailViewModel::class)
    abstract fun provideEventDetailViewModel(eventDetail: EventDetailViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory @Inject
    constructor(private val creators: Map<Class<out ViewModel>,
            @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {

        @NonNull
        override fun <T : ViewModel> create(@NonNull modelClass: Class<T>): T {
            var creator: Provider<out ViewModel>? = creators[modelClass]
            if (creator == null) {
                for ((key, value) in creators) {
                    if (modelClass.isAssignableFrom(key)) {
                        creator = value
                        break
                    }
                }
            }
            if (creator == null) {
                throw IllegalArgumentException("unknown model class $modelClass")
            }
            try {
                return creator.get() as T
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }
    }
}