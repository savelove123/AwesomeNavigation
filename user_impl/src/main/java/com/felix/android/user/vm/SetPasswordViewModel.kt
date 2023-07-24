package com.felix.android.user.vm

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.felix.android.base.NavViewModel
import com.felix.android.common.createActionDoneListener
import com.felix.android.common.createTextWatcher
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.demo.user.SetPasswordDestination
import com.felix.android.navigation.destination.NavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetPasswordViewModel @Inject constructor(
    application: Application
):NavViewModel<NavDestination>(application = application),Parameterized<SetPasswordDestination.Params> {


    val password = MutableLiveData("")

    fun observePassword() = createTextWatcher {
        password.value = it?.toString()?:""
    }

    fun confirm() = createActionDoneListener{
        if( password.value.isNullOrEmpty() ){
            Toast.makeText( getApplication() , "密码不能为空" , Toast.LENGTH_SHORT ).show()
            return@createActionDoneListener
        }
        if( requireVMParams().token.isEmpty() ){
            Toast.makeText( getApplication() , "修改密码的token不能为空" , Toast.LENGTH_SHORT ).show()
            return@createActionDoneListener
        }
        navigateBack()
    }

}