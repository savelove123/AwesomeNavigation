package com.felix.android.user.destination

import android.content.Intent
import android.os.Parcelable
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthType
import com.felix.android.navigation.demo.user.SetPasswordDestination
import com.felix.android.navigation.demo.user.UserRepository
import com.felix.android.navigation.destination.Screen
import com.felix.android.navigation.destination.injectParams
import com.felix.android.user.ui.SetPasswordActivity
import javax.inject.Inject

//实现上是可以把部分的业务逻辑放入到导航当中，类似拦截器的功能，比如下面
//但是我强烈不建议下面这种把业务的逻辑放入到导航里面的方式，包括设置拦截器，这样破坏了业务代码的内聚性
//另外太多的公共内容会使得代码难以测试，比如全局拦截器这种，如果不是特别必要，不建议使用
//应该在设置参数的时候应该就做好了导航行为的判断，NavDestination只可包含UI相关的参数，不包含业务逻辑
class SetPasswordDestinationImpl @Inject constructor (
    private val authDestination: AuthDestination,
    private val userRepository: UserRepository
    ) :SetPasswordDestination{

    override fun toScreen(params: Parcelable?, navigable: Navigable): Screen {
        params?.let {
            return Intent( navigable.toContext(), SetPasswordActivity::class.java)
        }?: return authDestination.injectParams(
            AuthDestination.Params(
                AuthType.ResetPassword,
                userRepository.userInfo().value?.phone )
        ).toScreen(  navigable )
    }

}