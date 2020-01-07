package com.lab.esh1n.weather.di.module

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lab.esh1n.weather.di.base.ViewModelKey
import com.lab.esh1n.weather.di.weather.WeatherUseCasesModule
import com.lab.esh1n.weather.weather.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by esh1n on 3/9/18.
 */
@Module(includes = [WeatherUseCasesModule::class])
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(CurrentWeatherVM::class)
    abstract fun provideWeatherViewModel(weatherViewModel: CurrentWeatherVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashVM::class)
    abstract fun provideSplashVM(weatherVM: SplashVM): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(EmptyVM::class)
    abstract fun provideEmptyVM(weatherVM: EmptyVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun provideSettingsViewModel(weatherVM: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AllPlacesVM::class)
    abstract fun provideAllPlacesViewModel(weatherVM: AllPlacesVM): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(RouteVM::class)
    abstract fun provideRouteVM(weatherVM: RouteVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForecastWeekVM::class)
    abstract fun provideForecastVM(weatherVM: ForecastWeekVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DayForecastVM::class)
    abstract fun provideDayForecastVM(weatherVM: DayForecastVM): ViewModel

    @Singleton
    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory @Inject
    constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {

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