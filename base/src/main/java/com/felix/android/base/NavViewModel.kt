package com.felix.android.base

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.getParams
import androidx.lifecycle.setParams
import androidx.lifecycle.viewModelScope
import com.felix.android.navigation.NavComponent
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.ext.MutableLiveEvent
import com.felix.android.navigation.model.NavigationCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * 支持导航的ViewModel
 */
abstract class NavViewModel<T: NavDestination>(application: Application): BaseVM(application),
    NavComponent<T> {

    override val navigableScope: CoroutineScope = viewModelScope
    override val router: MutableLiveEvent<NavigationCommand> = MutableLiveEvent()

    override var activityResultLauncher:ActivityResultLauncher<Intent>?=null
    override var resultFlow: MutableSharedFlow<ActivityResult>? = null

    fun <T:Parcelable> Parameterized<T>.requireVMParams():T{
        return getParams()
    }

    fun <T:Parcelable> Parameterized<T>.injectVMParams( params:T? ){
        setParams( params )
    }

    override fun attachViewModel(savedInstanceState: Bundle?){

    }

}


/**
 * 通过Hilt依赖注入来懒加载一个[NavViewModel]，
 * 并且支持在创建后执行一些初始化操作
 */
inline fun <reified T> AppCompatActivity.lazyNavViewModel(
    crossinline initializer: T.() -> Unit = {}
): Lazy<T> where T : NavViewModel<*>, T : NavComponent<*> {
    return lazy {
        val viewModel :T by viewModels()
        initializer(viewModel)
        viewModel
    }
}

inline fun <reified T> Fragment.lazyNavViewModel(
    crossinline initializer: T.() -> Unit = {}
): Lazy<T> where T : NavViewModel<*>, T : NavComponent<*> {
    return lazy {
        val viewModel :T by viewModels()
        initializer(viewModel)
        viewModel
    }
}
