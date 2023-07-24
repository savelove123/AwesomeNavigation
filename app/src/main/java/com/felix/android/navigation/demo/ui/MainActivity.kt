package com.felix.android.navigation.demo.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthRepository
import com.felix.android.navigation.auth.AuthType
import com.felix.android.navigation.demo.nav.FragmentNavigatorDelegate
import com.felix.android.navigation.demo.R
import com.felix.android.navigation.demo.databinding.ActivityMainBinding
import com.felix.android.navigation.demo.user.UserRepository
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.destination.injectParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigable {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var authDestination: AuthDestination

    @Inject
    lateinit var userRepository: UserRepository


    override fun navigateTo(destination: NavDestination) {
        val destinationWrapper = destination.let {
            if (it is AuthDestination) {
                it.injectParams(
                    AuthDestination.Params(
                        if (userRepository.privacyAcceptState().value) {
                            AuthType.Login
                        } else AuthType.Register
                    )
                )
            } else {
                it
            }
        }
        super.navigateTo( destinationWrapper )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.addOnDestinationChangedListener{_,destination,_->
            if( destination.id == R.id.navigation_notifications && !authRepository.isAuthorized() ){
                navigateTo( authDestination )
            }
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}


