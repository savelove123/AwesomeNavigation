package com.felix.android.auth

import com.felix.android.auth.navigation.AuthDestinationImpl
import com.felix.android.auth.repository.AuthRepositoryImpl
import com.felix.android.base.AppScope
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository( @AppScope appScope: CoroutineScope):AuthRepository = AuthRepositoryImpl(appScope)

    @Provides
    fun provideAuthDestination( ): AuthDestination = AuthDestinationImpl()


}