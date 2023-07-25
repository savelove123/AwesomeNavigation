package com.felix.android.auth.ui

import android.os.Bundle
import android.util.Log
import com.felix.android.auth.impl.R
import com.felix.android.auth.impl.BR
import com.felix.android.auth.impl.databinding.ActivityAuthBinding
import com.felix.android.auth.vm.AuthViewModel
import com.felix.android.base.BaseActivity
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.auth.AuthDestination
import com.felix.android.navigation.auth.AuthType
import com.felix.android.navigation.ext.requireNavParam
import com.felix.android.navigation.lazyNavViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity: BaseActivity<ActivityAuthBinding,AuthViewModel>() ,Navigable{

    private val authViewModel :AuthViewModel by lazyNavViewModel {
        observeNavigation()
        injectVMParams(requireNavParam())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.title.setTitle( when( requireNavParam<AuthDestination.Params>().type){
            AuthType.Register->"注册"
            AuthType.Login->"登录"
            AuthType.ResetPassword->"重置密码"
            AuthType.ChangePhone->"修改手机号"
        })
        Log.d("AuthActivity","onCreate")
    }

    override fun getLayoutId() = R.layout.activity_auth

    override fun getBindingVM() = authViewModel
    override fun getVariableId() = BR.vm

}