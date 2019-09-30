package com.esh1n.core_android.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.esh1n.core_android.R

abstract class BaseToolbarActivity : BaseDIActivity() {

    protected var toolbar: Toolbar? = null

    protected open val isToolbarVisible = true

    protected open val isDisplayHomeAsUpEnabled = true

    protected open val isDisplayTitle = true

    abstract val toolbarId: Int

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
        toolbar = findViewById<View>(toolbarId) as Toolbar
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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_backward)
    }

    fun enableHomeAsUpButton(action: () -> Unit) {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_backward)
        }
        toolbar?.setNavigationOnClickListener { _ -> action.invoke() }
    }

    fun showHomeAsUpButton(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }
}