package com.felix.android.navigation.demo.user

import com.felix.android.navigation.demo.user.model.UserInfo
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull

interface UserRepository {

    fun userInfo(): StateFlow<UserInfo?>

    suspend fun setUserInfo( userInfo: UserInfo ):Result<String?>

    fun privacyAcceptState(): StateFlow<Boolean>

    fun acceptPrivacy(accepted:Boolean)

}