package com.esh1n.core_android.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.esh1n.core_android.ui.addSingleFragmentToContainer

abstract class SingleFragmentActivity : BaseToolbarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitContent(savedInstanceState)
    }


    protected fun onInitContent(savedInstanceState: Bundle?) {
        val startScreen = getStartScreen(savedInstanceState)
        if (startScreen != null) {
            this.addSingleFragmentToContainer(startScreen)
        }
    }

    protected abstract fun getStartScreen(savedInstanceState: Bundle?): Fragment?
}
