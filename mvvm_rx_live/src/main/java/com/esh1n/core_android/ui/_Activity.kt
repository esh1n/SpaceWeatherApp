package com.esh1n.core_android.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.esh1n.core_android.R

inline fun <reified T : Activity> Context?.startActivity() {
    this?.let {
        val intent = Intent(this, T::class.java)
        it.startActivity(intent)
    }
}

inline fun <reified T : Activity> Context?.startActivity(args: Bundle) {
    this?.let {
        val intent = Intent(this, T::class.java)
        intent.putExtras(args)
        it.startActivity(intent)
    }
}

fun FragmentActivity?.addSingleFragmentToContainer(fragment: Fragment, hide: Boolean = false, tag: String? = fragment.tag) {
    this?.let {
        val fragmentNotExist = supportFragmentManager.findFragmentById(R.id.container_fragment) == null
        if (fragmentNotExist) {
            addFragment(fragment, hide, tag)
        }
    }
}

fun FragmentActivity?.addFragment(fragment: Fragment, hide: Boolean = false, tag: String? = fragment.tag) {
    this?.let {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container_fragment, fragment, tag)
        if (hide) {
            transaction.hide(fragment)
        }
        transaction.commit()
    }
}

fun FragmentManager?.replaceFragment(fragment: Fragment, tag: String) {
    this?.let {
        val transaction = beginTransaction()
        transaction.replace(R.id.container_fragment, fragment, tag)
        transaction.commit()
    }
}

fun FragmentActivity?.addFragmentToStack(fragment: Fragment) {
    this?.let {
        val tag = fragment.javaClass.simpleName
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container_fragment, fragment, tag)
        transaction.addToBackStack(null).commit()
    }
}

fun AppCompatActivity.setABTitle(title: CharSequence?) {
    if (!title.isNullOrBlank()) {
        supportActionBar?.title = title
    }

}
