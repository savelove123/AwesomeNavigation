package com.felix.android.user.vm

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.felix.android.navigation.NavViewModel
import com.felix.android.common.createTextWatcher
import com.felix.android.home.HomeDestination
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.demo.user.UserRepository
import com.felix.android.navigation.demo.user.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetUserInfoViewModel @Inject constructor(
    application: Application,
    private val homeDestination: HomeDestination,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
    ) : NavViewModel<HomeDestination>(application) {

    val name = MutableLiveData("")

    val email = MutableLiveData("")

    fun observeName() = createTextWatcher {
        name.value = it?.toString() ?: ""
    }

    fun observeEmail() = createTextWatcher {
        email.value = it?.toString() ?: ""
    }

    override fun attachViewModel(savedInstanceState: Bundle?) {

    }

    fun saveUserInfo(){
        val userInfo = UserInfo(
            authRepository.authState().value.phoneNumber!!,
            name.value,
            email.value)
        viewModelScope.launch {
            userRepository.setUserInfo( userInfo ).fold( onSuccess = {
                it?.let {
                    Toast.makeText( getApplication(), it , Toast.LENGTH_SHORT ).show()
                }
                homeDestination.navigate()
                navigateBack()
            },onFailure = {
                Toast.makeText( getApplication(), it.message , Toast.LENGTH_SHORT ).show()
            })
        }

    }
}