package com.felix.android.user.destination

import android.content.Intent
import android.os.Parcelable
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthType
import com.felix.android.navigation.demo.user.ChangePhoneDestination
import com.felix.android.navigation.demo.user.UserRepository
import com.felix.android.navigation.destination.Screen
import com.felix.android.navigation.destination.injectParams
import com.felix.android.user.ui.ChangePhoneActivity
import javax.inject.Inject


class ChangePhoneDestinationImpl @Inject constructor(): ChangePhoneDestination {

    override fun toScreen( params: Parcelable?, navigable: Navigable): Screen {
        requireNotNull(params)
        return Intent( navigable.toContext(), ChangePhoneActivity::class.java )
    }
}