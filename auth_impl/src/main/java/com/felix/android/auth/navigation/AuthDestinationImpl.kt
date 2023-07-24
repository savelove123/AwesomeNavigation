package com.felix.android.auth.navigation

import android.content.Intent
import android.os.Parcelable
import com.felix.android.auth.ui.AuthActivity
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.destination.Screen

class AuthDestinationImpl: AuthDestination{
    override fun toScreen( params: Parcelable?, navigable: Navigable): Screen {
        return Intent(navigable.toContext(), AuthActivity::class.java)
    }
}