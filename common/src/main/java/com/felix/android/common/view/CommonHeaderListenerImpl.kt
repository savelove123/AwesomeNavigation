package com.felix.android.common.view

import android.app.Activity
import android.content.Context


class CommonHeaderListenerImpl(private val context: Context) : CommonHeaderListener {

    override fun onBackClick() {
         (context as? Activity)?.finish()
    }

    override fun onRightIconClick() {

    }
}
