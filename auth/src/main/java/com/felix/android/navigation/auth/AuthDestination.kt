package com.felix.android.navigation.auth

import android.os.Parcelable
import com.felix.android.navigation.destination.ScreenDestination
import kotlinx.parcelize.Parcelize

interface AuthDestination:ScreenDestination{

    @Parcelize
    data class Params(private val authType:AuthType, val phone:String?=null):Parcelable{
        val type :AuthType get() = run {
            if (authType != AuthType.Login && authType != AuthType.Register && phone.isNullOrEmpty()) {
                AuthType.Login
            } else {
                authType
            }
        }
    }
}
