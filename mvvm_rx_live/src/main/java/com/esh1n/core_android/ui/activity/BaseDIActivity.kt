package com.esh1n.core_android.ui.activity

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by esh1n on 3/9/18.
 */

abstract class BaseDIActivity : AppCompatActivity(), HasSupportFragmentInjector {

    abstract val contentViewResourceId: Int

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(contentViewResourceId)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return injector
    }
}