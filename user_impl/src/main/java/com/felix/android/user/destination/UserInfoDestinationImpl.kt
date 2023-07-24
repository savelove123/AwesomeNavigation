package com.felix.android.user.destination

import android.content.Intent
import android.os.Parcelable
import android.util.Log
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.auth.AuthType
import com.felix.android.navigation.demo.user.UserInfoDestination
import com.felix.android.navigation.demo.user.UserRepository
import com.felix.android.navigation.destination.NavDestinationContext
import com.felix.android.navigation.destination.Screen
import com.felix.android.navigation.destination.injectParams
import com.felix.android.user.ui.UserInfoActivity
import com.felix.android.user.ui.UserInfoFragment

class UserInfoDestinationImpl constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val authDestination: AuthDestination,
    ) : UserInfoDestination,
    Parameterized<UserInfoDestination.Params> {

    override val navContext: NavDestinationContext
        get() = run {
            authNavContext?:super.navContext
        }

    var authNavContext:NavDestinationContext? = null

    override fun toScreen(params: Parcelable?, navigable:Navigable): Screen {
        if( !authRepository.isAuthorized()  ){
            val loginParams =  AuthDestination.Params( if( userRepository.userInfo().value!= null ) AuthType.Login else AuthType.Register)
            authNavContext = NavDestinationContext( loginParams)
            return authDestination.injectParams(loginParams )
                .toScreen( navigable )
        }

        return if( params.reifiedOrNull()?.requireFragment == true  ){
            UserInfoFragment()
        }else {
            Intent( navigable.toContext(), UserInfoActivity::class.java )
        }
    }

}
