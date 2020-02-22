package com.lab.esh1n.weather.weather

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.activity.BaseToolbarActivity
import com.esh1n.core_android.ui.replaceFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.weather.fragment.SplashFragment
import com.lab.esh1n.weather.weather.fragment.WeatherHostFragment
import com.lab.esh1n.weather.weather.viewmodel.RouteVM
import java.util.*
import javax.inject.Inject

/**
 * Created by esh1n on 3/16/18.
 */

class WeatherActivity : BaseToolbarActivity(), AppView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewModel: RouteVM
            by lazy {
                ViewModelProviders.of(this, viewModelFactory)
                        .get(RouteVM::class.java)
            }

    override val toolbarId = R.id.toolbar
    override val contentViewResourceId = R.layout.activity_main
    override val isDisplayHomeAsUpEnabled = false

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        viewModel.dataWasInitializedEvent.observe(this, object : BaseObserver<Boolean>() {
            override fun onError(error: ErrorModel?) {

                SnackbarBuilder.showErrorSnackOrToast(this@WeatherActivity, error!!.message)
            }

            override fun onData(initialized: Boolean?) {
                initialized?.let {
                    logRouteEvent(initialized)
                    val currentFragment = supportFragmentManager.findFragmentById(com.esh1n.core_android.R.id.container_fragment)
                    val noCurrentFragmentExist = currentFragment == null
                    if (noCurrentFragmentExist || (initialized && currentFragment !is WeatherHostFragment)) {
                        FirebaseCrashlytics.getInstance().log("added new fragment on WeatherActivity")
                        val fragment = if (initialized) WeatherHostFragment.newInstance() else SplashFragment.newInstance()
                        supportFragmentManager.replaceFragment(fragment, fragment::class.java.simpleName)
                    } else {
                        FirebaseCrashlytics.getInstance().log("can not add new fragment on WeatherActivity")
                    }
                }
            }

        })
        findViewById<Button>(R.id.bt_recreate).setOnClickListener {
            recreate()
        }
        viewModel.checkIfInitialized()
        initFragmentTransactionsListener()

    }

    override fun onResume() {
        super.onResume()
        logOnResumeEvent()
    }

    //TODO move to own analytics holder
    private fun logRouteEvent(isDataInitialized: Boolean) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "main_activity")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "on_route_data")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, isDataInitialized.toString())
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun logOnResumeEvent() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "main_activity")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "on_resume")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "-what?")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun initFragmentTransactionsListener() {
        supportFragmentManager.addOnBackStackChangedListener { this.processFragmentsSwitching() }
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

    override fun applySelectedAppLanguage(context: Context): Context {
        val app = context.applicationContext as? WeatherApp
        app?.let {
            val locale = it.getLocaleBlocking()
            val newConfig = Configuration(context.resources.configuration)
            Locale.setDefault(locale)
            newConfig.setLocale(locale)
            return context.createConfigurationContext(newConfig)
        }
        return context

    }


}