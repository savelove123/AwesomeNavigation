package com.felix.android.user.destination

import android.content.Intent
import android.os.Parcelable
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.demo.user.SetUserInfoDestination
import com.felix.android.navigation.destination.Screen
import com.felix.android.user.ui.SetUserInfoActivity
import javax.inject.Inject

class SetUserInfoDestinationImpl @Inject constructor() :SetUserInfoDestination{

    override fun toScreen(params: Parcelable?, navigable: Navigable): Screen {
        return Intent( navigable.toContext(), SetUserInfoActivity::class.java)
    }

}