package com.felix.android.user.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.felix.android.base.NavViewModel
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.demo.user.PrivacyDestination
import com.felix.android.navigation.demo.user.UserRepository
import com.felix.android.navigation.destination.NavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository
    ):NavViewModel<NavDestination>( application ), Parameterized<PrivacyDestination.Params> {



    val showConfirm by lazy {
        MutableLiveData( requireVMParams().action == PrivacyDestination.Action.CONFIRM )
    }

    fun accept(){
        userRepository.acceptPrivacy(true)
        finish( PrivacyDestination.Results(true) )
    }

    fun reject(){
        userRepository.acceptPrivacy(false)
        finish( PrivacyDestination.Results(false) )
    }

}