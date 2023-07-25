package com.felix.android.auth.vm

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.felix.android.navigation.NavViewModel
import com.felix.android.common.createActionDoneListener
import com.felix.android.common.createTextWatcher
import com.felix.android.home.HomeDestination
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.auth.AuthType
import com.felix.android.navigation.auth.model.AuthResult
import com.felix.android.navigation.demo.user.PrivacyDestination
import com.felix.android.navigation.demo.user.SetPasswordDestination
import com.felix.android.navigation.demo.user.SetUserInfoDestination
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.destination.injectParams
import com.felix.android.navigation.model.NavigationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthNextDestination:NavDestination

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository,
    val setPasswordDestination: SetPasswordDestination,
    val setUserInfoDestination: SetUserInfoDestination,
    val privacyDestination: PrivacyDestination,
    val homeDestination: HomeDestination
    ): NavViewModel<AuthNextDestination>( application = application ),Parameterized<AuthDestination.Params>{


    val phone = MutableLiveData("")
    val verifyCode = MutableLiveData("")

    override fun attachViewModel(savedInstanceState: Bundle?) {

        if( requireVMParams().type == AuthType.Register ){
            viewModelScope.launch {
                val results :PrivacyDestination.Results? = privacyDestination.injectParams(PrivacyDestination.Params( PrivacyDestination.Action.CONFIRM ))
                    .navigateForResult().let {
                        NavigationResult( it.code,it.data)
                    }.getParcelable()
                if(results?.accepted != true ){
                    navigateBack()
                }
            }

        }
    }

    fun observePhone() = createTextWatcher {
        phone.value = it?.toString() ?: ""
    }

    fun observeVerifyCode() = createTextWatcher {
        verifyCode.value = it?.toString() ?: ""
    }

    fun verify() = createActionDoneListener{
        viewModelScope.launch {
            authRepository.verifyPhone( phone.value!! , verifyCode.value!! , requireVMParams().type )
                .fold( onSuccess = {
                    when( requireVMParams().type ){
                        AuthType.Register->{
                            authRepository.setAuthState( it  as AuthResult.AuthState)
                            SetUserInfo().navigate()
                        }
                        AuthType.ResetPassword->{
                            SetPassword()
                                .injectParams( SetPasswordDestination.Params(( it as AuthResult.CheckOperation).token))
                                .navigate()
                        }
                        AuthType.ChangePhone->{
                            Log.d("ChangePhone","finish result :$it")
                            finish( it )
                        }
                        AuthType.Login->{
                            authRepository.setAuthState( it  as AuthResult.AuthState)
                            navigateToPrevious( Home() )
                        }
                    }
                    navigateBack()
                }, onFailure = {
                    Toast.makeText( getApplication() , it.message , Toast.LENGTH_SHORT ).show()
                })
        }


    }

    inner class SetPassword : AuthNextDestination(),SetPasswordDestination by setPasswordDestination
    inner class SetUserInfo : AuthNextDestination(),SetUserInfoDestination by setUserInfoDestination
    inner class Home:AuthNextDestination(),HomeDestination by homeDestination

}