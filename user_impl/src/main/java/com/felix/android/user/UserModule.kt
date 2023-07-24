package com.felix.android.user

import com.felix.android.base.AppScope
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.demo.user.ChangePhoneDestination
import com.felix.android.navigation.demo.user.PrivacyDestination
import com.felix.android.navigation.demo.user.SetPasswordDestination
import com.felix.android.navigation.demo.user.SetUserInfoDestination
import com.felix.android.navigation.demo.user.UserInfoDestination
import com.felix.android.navigation.demo.user.UserRepository
import com.felix.android.user.destination.ChangePhoneDestinationImpl
import com.felix.android.user.destination.PrivacyDestinationImpl
import com.felix.android.user.destination.SetPasswordDestinationImpl
import com.felix.android.user.destination.SetUserInfoDestinationImpl
import com.felix.android.user.destination.UserInfoDestinationImpl
import com.felix.android.user.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        authRepository: AuthRepository,
        @AppScope scope: CoroutineScope,
    ): UserRepository = UserRepositoryImpl(authRepository, scope)

    @Provides
    fun provideChangePhoneDestination(): ChangePhoneDestination = ChangePhoneDestinationImpl()

    @Provides
    fun providePrivacyDestination(): PrivacyDestination = PrivacyDestinationImpl()

    @Provides
    fun provideSetUserInfoDestination(): SetUserInfoDestination = SetUserInfoDestinationImpl()

    @Provides
    fun provideSetPasswordDestination(
        authDestination: AuthDestination,
        userRepository: UserRepository,
    ): SetPasswordDestination = SetPasswordDestinationImpl(authDestination, userRepository)

    @Provides
    fun provideUserInfoDestination(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        authDestination: AuthDestination,
    ): UserInfoDestination = UserInfoDestinationImpl( authRepository,userRepository,authDestination)


}