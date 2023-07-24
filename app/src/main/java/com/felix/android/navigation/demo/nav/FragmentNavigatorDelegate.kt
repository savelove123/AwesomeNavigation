package com.felix.android.navigation.demo.nav

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.demo.R

@Navigator.Name("fragment")
class FragmentNavigatorDelegate(
    private val navigable: Navigable,
    private val authRepository: AuthRepository,
    private val authDestination: AuthDestination,
    private val fragmentNavigator: FragmentNavigator
    ) :Navigator<NavDestination>( ) {
    override fun createDestination(): NavDestination {
        return fragmentNavigator.createDestination()
    }

    override fun navigate(
        destination: NavDestination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ): NavDestination? {
        if( destination.id == R.id.navigation_notifications && !authRepository.isAuthorized() ){
            navigable.navigateTo( authDestination )
            return null
        }
        return fragmentNavigator.navigate(destination as FragmentNavigator.Destination ,  args, navOptions, navigatorExtras)
    }


    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ) {
        return fragmentNavigator.navigate(entries, navOptions, navigatorExtras)
    }

}