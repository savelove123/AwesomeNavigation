package com.felix.android.user.ui

import com.felix.android.base.BaseActivity
import com.felix.android.base.lazyNavViewModel
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.ext.requireNavParam
import com.felix.android.user.impl.R
import com.felix.android.user.impl.BR
import com.felix.android.user.impl.databinding.ActivityPrivacyBinding
import com.felix.android.user.vm.PrivacyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyActivity:BaseActivity<ActivityPrivacyBinding,PrivacyViewModel>(), Navigable {

    private val privacyViewModel :PrivacyViewModel by lazyNavViewModel{
        observeNavigation()
        injectVMParams( requireNavParam() )
    }

    override fun getLayoutId() = R.layout.activity_privacy

    override fun getBindingVM(): PrivacyViewModel = privacyViewModel

    override fun getVariableId() = BR.vm
}