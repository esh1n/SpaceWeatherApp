package com.esh1n.core_android.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.esh1n.core_android.R
import dagger.android.AndroidInjection

abstract class BaseDIActivity : AppCompatActivity() {

    protected open val contentViewResourceId = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(contentViewResourceId)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

}