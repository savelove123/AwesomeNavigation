package com.felix.android.navigation.demo.user

import android.os.Parcelable
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.destination.ScreenDestination
import kotlinx.parcelize.Parcelize

interface SetPasswordDestination :ScreenDestination,Parameterized<SetPasswordDestination.Params>{

    @Parcelize
    data class Params(val token:String ): Parcelable
}