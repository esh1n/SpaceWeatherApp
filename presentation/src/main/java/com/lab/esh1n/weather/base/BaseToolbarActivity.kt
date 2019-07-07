package com.lab.esh1n.weather.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.lab.esh1n.weather.R

abstract class BaseToolbarActivity : BaseActivity() {

    protected var toolbar: Toolbar? = null

    protected open val isToolbarVisible = true

    protected open val isDisplayHomeAsUpEnabled = true

    protected open val isDisplayTitle = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        supportActionBar?.let {
            if (isToolbarVisible) {
                it.show()
            } else {
                it.hide()
            }
        }
    }

    private fun setupToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.let {
                onSetupActionBar(it)
            }
            onSetupToolbar(toolbar!!)
        }
    }

    protected fun onSetupToolbar(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener { _ -> onBackPressed() }
    }

    protected open fun onSetupActionBar(actionBar: ActionBar) {
        actionBar.setDisplayShowTitleEnabled(isDisplayTitle)
        actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_active)
    }

    fun enableHomeAsUpButton(action: () -> Unit) {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_close_white_active)
        }
        toolbar?.setNavigationOnClickListener { _ -> action.invoke() }
    }

    fun disableHomeAsUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}