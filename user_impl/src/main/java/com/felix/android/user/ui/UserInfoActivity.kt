package com.felix.android.user.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.felix.android.base.BaseActivity
import com.felix.android.navigation.lazyNavViewModel
import com.felix.android.navigation.FragmentNavComponent
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.dsl.doWhen
import com.felix.android.navigation.ext.requireFragment
import com.felix.android.navigation.ext.requireNavParam
import com.felix.android.user.impl.R
import com.felix.android.user.impl.BR
import com.felix.android.user.impl.databinding.ActivityUserInfoBinding
import com.felix.android.user.vm.UserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoActivity:BaseActivity<ActivityUserInfoBinding,UserInfoViewModel>(),Navigable,FragmentNavComponent{

    private val userInfoViewModel: UserInfoViewModel by lazyNavViewModel{
        observeNavigation()
        injectVMParams(requireNavParam())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun navigateTo(destination: NavDestination) {
        destination.doWhen {
            isFragment {
                supportFragmentManager.beginTransaction()
                    .add( R.id.fl_content,destination.requireFragment() )
                    .commit()
            }
            isIntent {
                super.navigateTo(destination)
            }
            isFragmentDialog {
                super.navigateTo(destination)
            }
            otherDestination {
                super.navigateTo(destination)
            }
        }
    }

    override fun navigate(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add( R.id.fl_content,fragment )
            .commit()
    }

    override fun fragmentNavigator(): FragmentNavComponent{
        return this
    }

    override fun getLayoutId(): Int = R.layout.activity_user_info

    override fun getBindingVM(): UserInfoViewModel = userInfoViewModel

    override fun getVariableId(): Int {
        return BR.vm
    }

}