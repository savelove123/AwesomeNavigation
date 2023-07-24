package com.felix.android.navigation.demo

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.felix.android.base.AppScope
import com.felix.android.home.HomeDestination
import com.felix.android.navigation.demo.nav.HomeDestinationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @AppScope
    @Singleton
    fun provideAppScope():CoroutineScope = ProcessLifecycleOwner.get().lifecycleScope

    @Provides
    fun provideHomeDestination():HomeDestination = HomeDestinationImpl()
}