package com.felix.android.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.felix.android.navigation.NavViewModel

abstract class BaseActivity<V : ViewDataBinding, M : NavViewModel<*>> : AppCompatActivity(){

    protected lateinit var viewDataBinding: V
    protected lateinit var viewModel: M


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = getBindingVM()
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.setVariable( getVariableId(), viewModel )
        viewDataBinding.executePendingBindings()
        viewModel.attachViewModel(savedInstanceState)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getBindingVM(): M

    abstract fun getVariableId(): Int

}