/*
 * Copyright 2023 , Felix Huang and the AwesomeNavigation project contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("UNCHECKED_CAST")

package com.felix.android.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NavUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.felix.android.navigation.destination.InjectedParamsDestination
import com.felix.android.navigation.ext.observeAndConsumeNonNull
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.destination.Screen
import com.felix.android.navigation.destination.ScreenDestination
import com.felix.android.navigation.dsl.doWhen
import com.felix.android.navigation.ext.getFragmentTag
import com.felix.android.navigation.ext.illegal
import com.felix.android.navigation.ext.requireDialogFragment
import com.felix.android.navigation.ext.requireFragment
import com.felix.android.navigation.ext.requireIntent
import com.felix.android.navigation.ext.unsupported
import com.felix.android.navigation.model.NavigationCommand
import com.felix.android.navigation.model.NavigationResult
import com.felix.android.navigation.model.NavigationResult.Companion.RESULT_PARAM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

interface FragmentNavComponent{
    fun navigate( fragment: Fragment )
}

internal class InternalNavDestination(
    private val current: Navigable,
    val destination: ScreenDestination ):ScreenDestination{
    override fun toScreen(params: Parcelable?, navigable: Navigable): Screen {
        return destination.toScreen(params,navigable)
    }
    override fun currentLocation(): Navigable {
        return current
    }
}
internal class InternalNavDestinationParameterized(private val current: Navigable,  destination:InjectedParamsDestination<Parcelable> )
    : InjectedParamsDestination<Parcelable>(destination.internalParam,destination){
    override fun toScreen(params: Parcelable?, navigable: Navigable): Screen {
        return destination.toScreen(params,navigable)
    }
    override fun currentLocation(): Navigable {
        return current
    }
}

/**
 * 表示是可以执行导航跳转的，一般来说，[Navigable]观察[NavComponent]的路由状态，然后执行导航跳转，
 * 所以实现类一般是[Fragment]或[FragmentActivity]
 * 如果想要实现自定义的类型都可以导航，那么需要覆盖或者修改本类的函数
 * 如果自定义的类型要实现导航，那么需要提供[androidx.lifecycle.LifecycleOwner]，
 * 并且要覆写或者修改[Navigable.navigateTo],[Navigable.navigateToForResult]等函数
 * 然后可能需要编写额外的扩展函数，添加类似[requireActivity] [requireFragment]的函数
 */
interface Navigable {

    /**
     * 提供一种快捷的方式通过 [Fragment] 或 [FragmentActivity] 准确地观察 [NavComponent.router]。
     * 如果你想将导航操作委派给另一个 [Navigable]，可以设置 [delegate]。
     */
    fun <T : NavDestination> NavComponent<T>.observeNavigation(delegate: Navigable? = null) {
        router.observeAndConsumeNonNull(getLifecycleOwner()) {
            val navigable = delegate ?: this@Navigable
            it.handleNavigation(navigableScope, navigable,this)
        }
        Log.d("Navigable"," this@Navigable  is FragmentActivity :${ this@Navigable is FragmentActivity}")
        if( this@Navigable is FragmentActivity || this@Navigable is Fragment){
           activityResultLauncher = getResultLauncher( navigableScope,this)
        }
    }

    fun  getResultLauncher( coroutineScope: CoroutineScope,navComponent: NavComponent<*>):ActivityResultLauncher<Intent> {
        return when (this) {
            is FragmentActivity -> {
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    coroutineScope.launch {
                        navComponent.resultFlow?.emit(result)
                    }
                }
            }

            is Fragment -> {
                registerForActivityResult( ActivityResultContracts.StartActivityForResult()){result->
                    coroutineScope.launch {
                        navComponent.resultFlow?.emit( result )
                    }
                }

            }

            else -> illegal("NavigateResult need a FragmentActivity or Fragment ")
        }
    }


    /**
     * 使用 [NavComponent.router] 导航到指定的目的地[NavDestination]
     */
    fun navigateTo(destination: NavDestination) {
        destination.wrapper().doWhen {
            isFragmentDialog {
                navigateToDialogFragment(requireDialogFragment())
            }
            isFragment {
                fragmentNavigator()?.navigate(requireFragment()) ?: illegal("You should manually implement 'fragmentNavigator()' if you want to navigate to a Fragment.")
            }
            isIntent {
                navigateToActivity(requireIntent())
            }
            otherDestination { unsupported() }
        }
    }
    /**
     * 把[ScreenDestination]封装成[InternalNavDestination]，当导航的时候，如果当前是[ScreenDestination]且没有指定目标的导航起点，则把当前的[Navigable]对象作为导航起点
     */
    private fun NavDestination.wrapper() :NavDestination{
        return if((this is ScreenDestination) && this.currentLocation() == null)
            if( this is InjectedParamsDestination<*> ) {
                InternalNavDestinationParameterized(
                    this@Navigable,
                    this as InjectedParamsDestination<Parcelable>
                )
            }
            else
                InternalNavDestination( this@Navigable, this@wrapper)
        else this
    }

    /**
     * 通过 [NavComponent.router] 导航到指定屏幕，并且等待返回[NavigationResult]，支持协程
     */
    suspend fun navigateToForResult(
        destination: NavDestination,
        continuation: Continuation<NavigationResult>,
        navComponent: NavComponent<*>
    ) {
        coroutineScope {
            destination.wrapper().doWhen {
                isFragment {
                    illegal("navigateToForResult() is only available by [Intent]")
                }
                isIntent {
                    launch {
                        navComponent.activityResultLauncher?.launch( requireIntent() )
                        Log.d("Navigable","continuation->resume() ${navComponent.activityResultLauncher}")
                        navComponent.resultFlow?.first().let {result->
                            Log.d("Navigable","navComponent.resultFlow?.first() :${result}")
                            continuation.resume(
                                NavigationResult(code = result?.resultCode ?:Activity.RESULT_CANCELED, data = result?.data)
                            )
                        }
                    }
                }
                otherDestination { unsupported() }
            }
        }
    }

    /**
     * 获取支持导航到[Fragment]的导航器
     * 默认不支持
     * 可以覆盖这个函数实现
     */
    fun fragmentNavigator():FragmentNavComponent?{
        return null
    }

    /**
     * 快捷地让[NavComponent.router]导到之前的页面,
     * 这个方法主要在导航漏斗中使用，用来导航回到特定的界面，并关闭所有位于此界面后面的界面。
     * 比如下单流程中，用户在选择商品界面，点击取消按钮，需要导航回到商品列表界面，并关闭商品列表界面后面打开的所有界面。
     */
    fun navigateToPrevious(destination: NavDestination) {
        destination.wrapper().doWhen {
            isFragment {
                illegal("You should manually implement 'navigateToPrevious()' to navigate to a previous Fragment.")
            }
            isIntent {
                NavUtils.navigateUpTo(requireActivity(), requireIntent())
            }
            otherDestination { unsupported() }
        }
    }



    /**
     * 快速通过 [NavComponent.router]返回上一个页面，相当于返回键
     */
    fun navigateBack() {
        requireActivity().finish()
    }

    /**
     * 通过 [NavComponent.router] 关闭当前页面，并且设置结果(可选)
     */
    fun navigateFinish(results: Any?) {
        requireActivity().run {
            results?.let {
                val parcelables = when (it) {
                    is Parcelable -> listOf(it)
                    is List<*> -> it.filterIsInstance<Parcelable>()
                    else -> illegal(
                        errorMessage = "You should pass to the finish() method " +
                                "only 'Parcelable' or 'List<Parcelable>' variable types."
                    )
                }

                val intent =
                    Intent().putParcelableArrayListExtra(RESULT_PARAM, ArrayList(parcelables))
                Log.d("Navigable","set result :${results}")
                setResult(Activity.RESULT_OK, intent)
            }
            finishAfterTransition()
        }
    }
    //处理导航的命令,如果需要扩展导航命令，可以在这里扩展
    private fun NavigationCommand.handleNavigation(
        scope: CoroutineScope,
        navigable: Navigable,
        navComponent: NavComponent<*>
    ) {
        when (this) {
            is NavigationCommand.To -> navigable.navigateTo(destination.wrapper())
            is NavigationCommand.ToPrevious -> navigable.navigateToPrevious(destination.wrapper())
            is NavigationCommand.ForResult -> scope.launch { navigable.navigateToForResult(destination.wrapper(),continuation, navComponent) }
            is NavigationCommand.Back -> navigable.navigateBack()
            is NavigationCommand.Finish -> navigable.navigateFinish(results)
        }
    }

    private fun getLifecycleOwner() = when (this) {
        is FragmentActivity -> this
        is Fragment -> viewLifecycleOwner
        else -> illegal(CONTEXT_ERROR)
    }

    private fun requireActivity(): Activity = when (this) {
        is FragmentActivity -> this
        is Fragment -> this.requireActivity()
        is ContextProvider->  (this.toContext() as? Activity?) ?: illegal(CONTEXT_ERROR)
        else ->  illegal(CONTEXT_ERROR)
    }

    private fun navigateToActivity(intent: Intent) = when (this) {
        is FragmentActivity -> startActivity(intent)
        is Fragment -> startActivity(intent)
        else -> illegal(CONTEXT_ERROR)
    }
    @SuppressLint("CommitTransaction")
    private fun navigateToDialogFragment(dialogFragment: DialogFragment) {
        val fragmentManager = when (this) {
            is FragmentActivity -> supportFragmentManager
            is Fragment -> parentFragmentManager
            else -> illegal(CONTEXT_ERROR)
        }
        fragmentManager.beginTransaction().run {
            dialogFragment.show(this, dialogFragment.getFragmentTag())
        }
    }

    fun toContext(): Context {
        return when( this ){
            is ContextProvider -> this.getContext()
            is Context-> this
            //下面这行可以删了，因为如果是Fragment会被覆盖
            is Fragment-> this.requireContext()
            else -> illegal(CONTEXT_ERROR)
        }
    }

    companion object {
        private const val CONTEXT_ERROR =
            "This context is not handled... The Navigable interface supports only [Fragment] & [FragmentActivity]" +
                    " (or [ContextAware] objects if they only want to launch an activity)"
    }
}