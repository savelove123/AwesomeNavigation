package com.felix.android.navigation.demo.user

import android.os.Parcelable
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.destination.ScreenDestination
import kotlinx.parcelize.Parcelize

interface ChangePhoneDestination:ScreenDestination, Parameterized<ChangePhoneDestination.Params> {
    @Parcelize
    data class Params(val phone:String,val token:String ): Parcelable
}