package com.esh1n.core_android.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.esh1n.core_android.ui.addFragment

abstract class SingleFragmentActivity : BaseFragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitContent(savedInstanceState)
    }


    protected fun onInitContent(savedInstanceState: Bundle?) {
        val startScreen = getStartScreen(savedInstanceState)
        if (startScreen != null) {
            this.addFragment(startScreen)
        }
    }

    protected abstract fun getStartScreen(savedInstanceState: Bundle?): Fragment?
}
