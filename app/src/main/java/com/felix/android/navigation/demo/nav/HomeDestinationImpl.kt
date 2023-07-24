package com.felix.android.navigation.demo.nav

import android.content.Intent
import android.os.Parcelable
import com.felix.android.home.HomeDestination
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.demo.ui.MainActivity
import com.felix.android.navigation.destination.Screen
import javax.inject.Inject

class HomeDestinationImpl @Inject constructor(): HomeDestination {
    override fun toScreen( params:Parcelable?, navigable: Navigable): Screen {
        return Intent( navigable.toContext(), MainActivity::class.java )
    }
}