package com.felix.android.navigation.demo.user

import android.os.Parcelable
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.destination.ScreenDestination
import kotlinx.parcelize.Parcelize

interface PrivacyDestination :ScreenDestination, Parameterized<PrivacyDestination.Params> {

    @Parcelize
    data class Params(val action:Action): Parcelable

    @Parcelize
    data class Results(val accepted:Boolean): Parcelable

    enum class Action(val value:String){
        CONFIRM("confirm"),
        READ("read")
    }
}