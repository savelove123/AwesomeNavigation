package com.felix.android.user.ui

import android.os.Bundle
import com.felix.android.base.BaseActivity
import com.felix.android.navigation.lazyNavViewModel
import com.felix.android.navigation.Navigable
import com.felix.android.user.impl.R
import com.felix.android.user.impl.BR
import com.felix.android.user.impl.databinding.ActivitySetUserInfoBinding
import com.felix.android.user.vm.SetUserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetUserInfoActivity:BaseActivity<ActivitySetUserInfoBinding,SetUserInfoViewModel>(),Navigable {

    private val setUserInfoViewModel :SetUserInfoViewModel by lazyNavViewModel{
        observeNavigation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUserInfoViewModel.attachViewModel(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_set_user_info
    }

    override fun getBindingVM() = setUserInfoViewModel

    override fun getVariableId()=BR.vm

}