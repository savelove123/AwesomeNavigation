package com.felix.android.navigation.auth.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AuthResult{

    @Parcelize
    data class CheckOperation( val token: String ):AuthResult(),Parcelable

    @Parcelize
    data class AuthState( val isAuthorized: Boolean = false ,
                          val phoneNumber:String?=null,
                          val accessToken: String? = null,
                          val refreshToken: String? = null ):AuthResult(),Parcelable{
        companion object{
            val Empty = AuthState( false )
        }
    }
}

