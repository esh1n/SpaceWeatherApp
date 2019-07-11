package com.esh1n.core_android.ui.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import com.esh1n.core_android.R
import com.esh1n.utils_android.setVisibleOrGone
import java.util.*

abstract class BaseToolbarActivity : BaseDIActivity() {
    protected var toolbar: Toolbar? = null

    protected open val isToolbarVisible = true

    protected open val isDisplayHomeAsUpEnabled = false

    override val contentViewResourceId = R.layout.activity_base_toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        val toolbarBottomLine = findViewById<View>(R.id.view_toolbar)
        toolbarBottomLine.setVisibleOrGone(isToolbarVisible)
        if (isToolbarVisible) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    private fun setupToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar?.let {
            setSupportActionBar(toolbar)
            onSetupActionBar(supportActionBar)
            onSetupToolbar(it)
        }
    }

    protected fun onSetupToolbar(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener { _ ->
            onBackPressed()
        }
    }

    protected open fun onSetupActionBar(actionBar: ActionBar?) {
        if (actionBar == null) {
            return
        }
        with(actionBar) {
            setDisplayShowTitleEnabled(isDisplayHomeAsUpEnabled)
            setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled)
            setHomeAsUpIndicator(R.drawable.ic_arrow_backward)
        }
    }


    fun enableHomeAsUpButton(action: () -> Unit) {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_backward)
        }
        toolbar?.setNavigationOnClickListener { _ -> action.invoke() }
    }

    fun disableHomeAsUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
