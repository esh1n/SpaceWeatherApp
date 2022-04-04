package com.lab.esh1n.weather.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.esh1n.core_android.ui.activity.BaseToolbarActivity
import com.esh1n.core_android.ui.replaceFragment
import com.esh1n.utils_android.ui.ContextUtil.getLocalizedContext
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.presentation.fragment.SplashFragment
import com.lab.esh1n.weather.presentation.fragment.WeatherHostFragment
import com.lab.esh1n.weather.presentation.viewmodel.RouteVM
import javax.inject.Inject

/**
 * Created by esh1n on 3/16/18.
 */

class MainActivity : BaseToolbarActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val routeVM: RouteVM by viewModels { viewModelFactory }

    private val languageChangeVM: LanguageChangeVM by viewModels { viewModelFactory }

    override val toolbarId = R.id.toolbar
    override val contentViewResourceId = R.layout.activity_main
    override val isDisplayHomeAsUpEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        languageChangeVM.langChangeEffect.observe(this, ::recreate)
        routeVM.dataWasInitializedEvent.observe(this) { initialized ->
            val currentFragment =
                supportFragmentManager.findFragmentById(com.esh1n.core_android.R.id.container_fragment)
            val noCurrentFragmentExist = currentFragment == null
            if (noCurrentFragmentExist || (initialized && currentFragment !is WeatherHostFragment)) {
                val fragment =
                    if (initialized) WeatherHostFragment.newInstance() else SplashFragment.newInstance()
                supportFragmentManager.replaceFragment(
                    fragment,
                    fragment::class.java.simpleName
                )
            }
        }

        routeVM.checkIfInitialized()
        initFragmentTransactionsListener()

    }

    private fun initFragmentTransactionsListener() {
        supportFragmentManager.addOnBackStackChangedListener { processFragmentsSwitching() }
    }

    private fun processFragmentsSwitching() {
        supportFragmentManager.let {
            val isInRootFragment = it.backStackEntryCount == 0
            this.showHomeAsUpButton(!isInRootFragment)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(applySelectedAppLanguage(base))
    }

    private fun applySelectedAppLanguage(context: Context): Context {
        //TODO refactor to use like here https://stackoverflow.com/a/70301479
        val locale = (context.applicationContext as WeatherApp).getLocale()
        return getLocalizedContext(context, locale)
    }
}