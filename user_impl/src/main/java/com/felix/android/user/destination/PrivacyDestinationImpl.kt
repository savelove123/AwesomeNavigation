package com.felix.android.user.destination

import android.content.Intent
import android.os.Parcelable
import android.util.Log
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.demo.user.PrivacyDestination
import com.felix.android.navigation.destination.Screen
import com.felix.android.user.ui.PrivacyActivity
import javax.inject.Inject

class PrivacyDestinationImpl @Inject constructor(): PrivacyDestination {
    override fun toScreen(params: Parcelable?, navigable: Navigable): Screen {
        return Intent( navigable.toContext(), PrivacyActivity::class.java)
    }
}