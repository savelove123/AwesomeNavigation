package com.felix.android.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.felix.android.navigation.lazyNavViewModel
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.ext.requireNavParam
import com.felix.android.user.impl.R
import com.felix.android.user.impl.databinding.FragmentUserInfoBinding
import com.felix.android.user.vm.UserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment:Fragment(), Navigable {

    private val userInfoViewModel :UserInfoViewModel by lazyNavViewModel {
        observeNavigation()
        injectVMParams( requireNavParam() )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding :FragmentUserInfoBinding= DataBindingUtil.inflate( LayoutInflater.from(requireActivity()),
            R.layout.fragment_user_info,container,false)
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        binding.vm = userInfoViewModel
        userInfoViewModel.attachViewModel( savedInstanceState )

        return binding.root
    }

    override fun navigateBack() {
        userInfoViewModel.phone.value = "----"
        userInfoViewModel.name.value = "----"
        userInfoViewModel.email.value = "----"
    }
}