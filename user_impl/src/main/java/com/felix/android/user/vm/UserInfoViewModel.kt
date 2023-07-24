package com.felix.android.user.vm

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.felix.android.base.NavViewModel
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.auth.AuthType
import com.felix.android.navigation.auth.model.AuthResult
import com.felix.android.navigation.demo.user.ChangePhoneDestination
import com.felix.android.navigation.demo.user.UserInfoDestination
import com.felix.android.navigation.demo.user.UserRepository
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.destination.injectParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository,
    private val authDestination: AuthDestination,
    private val authRepository: AuthRepository,
    private val userInfoDestination: UserInfoDestination,
    private val changePhoneDestination: ChangePhoneDestination
) : NavViewModel<NavDestination>(application) ,Parameterized<UserInfoDestination.Params>{


    val phone = MutableLiveData("----")
    val name =  MutableLiveData("----")
    val email =  MutableLiveData("----")

    private val showLogout = MutableLiveData(false)


    override fun attachViewModel(savedInstanceState: Bundle?) {
        super.attachViewModel(savedInstanceState)

        if( !requireVMParams().requireFragment ){
            userInfoDestination
                .injectParams( UserInfoDestination.Params( true))
                .navigate()
        }

        showLogout.value = !requireVMParams().requireFragment

        userRepository.userInfo().onEach {
            it?.let {
                phone.value = it.phone
                name.value = it.name
                email.value = it.email
            }?:navigateBack()
        }.launchIn(viewModelScope)

        authRepository.authState().onEach {
            if( !it.isAuthorized ) {
                navigateBack()
            }
        }.launchIn(viewModelScope)

    }

    fun changePhone(){
        viewModelScope.launch {
            val result : AuthResult.CheckOperation? = authDestination.injectParams( AuthDestination.Params(
                AuthType.ChangePhone,
                authRepository.authState().value.phoneNumber) ).navigateForResult()
                .getParcelable()
            result?.let {
                changePhoneDestination.injectParams( ChangePhoneDestination.Params(
                    authRepository.authState().value.phoneNumber?:"",result.token
                )).navigate()
                Log.d("ChangePhone","params ${ChangePhoneDestination.Params(
                    authRepository.authState().value.phoneNumber?:"",result.token
                )}")

            }
        }
    }

    fun logout(){
        authRepository.invalidate()
    }
}