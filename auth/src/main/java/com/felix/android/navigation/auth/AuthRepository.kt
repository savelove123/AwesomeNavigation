package com.felix.android.navigation.auth

import com.felix.android.navigation.auth.model.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    fun authState(): StateFlow<AuthResult.AuthState>

    fun onAuthStateChanged():Flow<AuthResult.AuthState>

    fun isAuthorized(): Boolean

    fun setAuthState(authState: AuthResult.AuthState)

    fun invalidate()

    suspend fun verifyPhone( phone:String,smsCode:String,authType: AuthType ): Result<AuthResult>

}