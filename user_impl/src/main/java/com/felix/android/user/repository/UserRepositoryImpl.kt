package com.felix.android.user.repository

import com.felix.android.base.AppScope
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.demo.user.UserRepository
import com.felix.android.navigation.demo.user.model.UserInfo
import com.felix.android.navigation.ext.illegal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserRepositoryImpl constructor(
    private val authRepository: AuthRepository,
    private val appScope:CoroutineScope):UserRepository {

    private val userInfoFlow = MutableStateFlow<UserInfo?>( null )
    private val privacyAcceptedFlow = MutableStateFlow( false )

    init {
        appScope.launch {
            //当修改手机号的时候，同步修改用户信息

            authRepository.authState().filter {
                 it.isAuthorized&& it.phoneNumber != userInfoFlow.value?.phone && userInfoFlow.value != null
            }.map { it.phoneNumber }.onEach {
                it?.let {
                    userInfoFlow.emit( userInfoFlow.value!!.copy( phone = it ) )
                }?:userInfoFlow.emit( null )
            }.launchIn( appScope )
        }
    }

    override fun userInfo(): StateFlow<UserInfo?> = userInfoFlow.asStateFlow()

    override suspend fun setUserInfo(userInfo: UserInfo) :Result<String?>{
        //mock网络延迟
        delay(300L)

        return runCatching {
            checkUserInfo( userInfo ).apply {
                userInfoFlow.emit( userInfo )
            }
        }
    }

    override fun privacyAcceptState(): StateFlow<Boolean> = privacyAcceptedFlow.asStateFlow()

    override fun acceptPrivacy(accepted: Boolean) {
        privacyAcceptedFlow.tryEmit( accepted )
    }

    private fun checkUserInfo( userInfo: UserInfo ):String?{
        if ( userInfo.name.isNullOrEmpty() ){
            illegal("姓名不能为空")
        }
        if ( userInfo.phone.isEmpty() ){
            illegal("手机号不能为空")
        }
        if( userInfo.phone != authRepository.authState().value.phoneNumber ){
            illegal("手机号不匹配")
        }
        return userInfo.email.let {
            if( it.isNullOrEmpty() ){
                "您未设置邮箱"
            }else{
                null
            }
        }
    }
}