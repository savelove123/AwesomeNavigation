package com.felix.android.auth.repository

import com.felix.android.base.AppScope
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.auth.AuthType
import com.felix.android.navigation.auth.model.AuthResult
import com.felix.android.navigation.ext.illegal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @AppScope private val appScope: CoroutineScope): AuthRepository {

    private val _authState = MutableStateFlow(AuthResult.AuthState.Empty)

    override fun authState(): StateFlow<AuthResult.AuthState> = _authState.asStateFlow()

    override fun onAuthStateChanged() = authState().drop(1)

    override fun isAuthorized() = authState().value.isAuthorized

    override fun setAuthState(authState: AuthResult.AuthState) {
        appScope.launch {
            _authState.emit(authState)
        }
    }

    override fun invalidate() {
        appScope.launch {
            _authState.emit(_authState.value.copy(isAuthorized = false))
        }
    }

    override suspend fun verifyPhone(phone: String, smsCode: String,authType: AuthType): Result<AuthResult> {
        return runCatching {
            if( phone.length !=11 ){
                illegal( "手机号码格式错误")
            }
            if( smsCode.length != 4 ){
                illegal( "验证码格式错误")
            }
            if( AuthType.Login == authType && phone!=_authState.value.phoneNumber ){
                illegal("手机号码不一致")
            }
            when( authType ){
                AuthType.Login,AuthType.Register->{
                    AuthResult.AuthState(true,phone,"valid_access_token","valid_refresh_token")
                }
                AuthType.ChangePhone->{
                    AuthResult.CheckOperation("changePhoneToken")
                }
                AuthType.ResetPassword->{
                    AuthResult.CheckOperation("resetPasswordToken")
                }
            }

        }


    }
}