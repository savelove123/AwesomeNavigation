package com.felix.android.user.ui

import com.felix.android.base.BaseActivity
import com.felix.android.base.lazyNavViewModel
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.ext.requireNavParam
import com.felix.android.user.impl.BR
import com.felix.android.user.impl.R
import com.felix.android.user.impl.databinding.ActivitySetPasswordBinding
import com.felix.android.user.vm.SetPasswordViewModel

class SetPasswordActivity:BaseActivity<ActivitySetPasswordBinding,SetPasswordViewModel>(), Navigable {

    private val setPasswordViewModel :SetPasswordViewModel by lazyNavViewModel{
        observeNavigation()
        injectVMParams( requireNavParam() )
    }

    override fun getLayoutId() = R.layout.activity_set_password

    override fun getBindingVM() = setPasswordViewModel

    override fun getVariableId() = BR.vm


}