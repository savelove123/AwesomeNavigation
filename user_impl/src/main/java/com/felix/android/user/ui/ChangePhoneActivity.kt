package com.felix.android.user.ui

import com.felix.android.base.BaseActivity
import com.felix.android.navigation.lazyNavViewModel
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.ext.requireNavParam
import com.felix.android.user.impl.R
import com.felix.android.user.impl.BR
import com.felix.android.user.impl.databinding.ActivityChangePhoneBinding
import com.felix.android.user.vm.ChangePhoneViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePhoneActivity:BaseActivity<ActivityChangePhoneBinding,ChangePhoneViewModel>(), Navigable {

    private val changePhoneViewModel :ChangePhoneViewModel by lazyNavViewModel{
        observeNavigation()
        injectVMParams( requireNavParam() )
    }

    override fun getLayoutId() = R.layout.activity_change_phone

    override fun getBindingVM() = changePhoneViewModel

    override fun getVariableId() = BR.vm


}