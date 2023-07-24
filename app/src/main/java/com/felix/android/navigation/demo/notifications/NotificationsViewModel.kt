package com.felix.android.navigation.demo.notifications

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.felix.android.base.NavViewModel
import com.felix.android.navigation.demo.user.SetUserInfoDestination
import com.felix.android.navigation.demo.user.UserInfoDestination
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.destination.injectParams
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    application: Application,
    private val userInfoDestination: UserInfoDestination,
) : NavViewModel<NavDestination>( application) {

    private val _text = MutableLiveData<String>().apply {
        value = "显示个人信息"
    }
    val text: LiveData<String> = _text

    fun showUserInfo(){
        userInfoDestination.injectParams(
            UserInfoDestination.Params( false )
        ).navigate()
    }
}