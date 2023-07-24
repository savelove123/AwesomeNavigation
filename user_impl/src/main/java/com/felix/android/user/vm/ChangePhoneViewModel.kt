package com.felix.android.user.vm

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.felix.android.base.NavViewModel
import com.felix.android.common.createActionDoneListener
import com.felix.android.common.createTextWatcher
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.demo.user.ChangePhoneDestination
import com.felix.android.navigation.destination.NavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePhoneViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository
):NavViewModel<NavDestination>(application),Parameterized<ChangePhoneDestination.Params> {

     val phone = MutableLiveData("")

    fun observePhone() = createTextWatcher {
        phone.value = it?.toString()?:""
    }

    fun confirm() = createActionDoneListener {
        if( phone.value.isNullOrEmpty() ){
            Toast.makeText( getApplication() , "手机号不能为空" , Toast.LENGTH_SHORT ).show()
            return@createActionDoneListener
        }
        if(requireVMParams().token.isEmpty()){
            Toast.makeText( getApplication() , "修改手机号的token不能为空" , Toast.LENGTH_SHORT ).show()
            return@createActionDoneListener
        }
        //修改手机号会改变authState中的手机号
        authRepository.setAuthState(authRepository.authState().value.copy( phoneNumber = phone.value ))
        navigateBack()
    }

}