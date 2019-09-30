package com.lab.esh1n.weather.weather

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.activity.BaseToolbarActivity
import com.esh1n.core_android.ui.replaceFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.fragment.SplashFragment
import com.lab.esh1n.weather.weather.fragment.WeatherHostFragment
import com.lab.esh1n.weather.weather.viewmodel.RouteVM
import javax.inject.Inject

/**
 * Created by esh1n on 3/16/18.
 */

class WeatherActivity : BaseToolbarActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dataWasInitializedEvent.observe(this, object : BaseObserver<Boolean>() {
            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(this@WeatherActivity, error!!.message).show()
            }

            override fun onData(initialized: Boolean?) {
                initialized?.let {
                    val currentFragment = supportFragmentManager.findFragmentById(com.esh1n.core_android.R.id.container_fragment)
                    val currentFragmentExist = currentFragment != null
                    if (!currentFragmentExist || (initialized && currentFragment !is WeatherHostFragment)) {
                        val fragment = if (initialized) WeatherHostFragment.newInstance() else SplashFragment.newInstance()
                        supportFragmentManager.replaceFragment(fragment, fragment::class.java.simpleName)
                    }
                }
            }

        })
        viewModel.checkIfInitialized()
        initFragmentTransactionsListener()
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


}