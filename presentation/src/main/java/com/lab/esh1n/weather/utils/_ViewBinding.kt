package com.lab.esh1n.weather.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> Fragment.autoDestroyViewDelegate(): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, DefaultLifecycleObserver {

        private var binding: T? = null

        init {
            // Observe the view lifecycle of the Fragment.
            // The view lifecycle owner is null before onCreateView and after onDestroyView.
            // The observer is automatically removed after the onDestroy event.
            this@autoDestroyViewDelegate
                .viewLifecycleOwnerLiveData
                .observe(this@autoDestroyViewDelegate) { owner: LifecycleOwner? ->
                    val lifecycle = owner?.lifecycle
                    lifecycle?.addObserver(this)
                }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                binding = null
            }

            override fun getValue(
                    thisRef: Fragment,
                    property: KProperty<*>
            ): T {
                return this.binding ?: error("Called before onCreateView or after onDestroyView.")
            }

            override fun setValue(
                    thisRef: Fragment,
                    property: KProperty<*>,
                    value: T
            ) {
                this.binding = value
            }
        }