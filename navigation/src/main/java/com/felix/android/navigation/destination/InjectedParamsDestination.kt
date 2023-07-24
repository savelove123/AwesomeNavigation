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

package com.felix.android.navigation.destination

import android.os.Parcelable
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.ext.EmptyNavContext

/**
 * 接收[Parcelable]类型的参数和一个[ScreenDestination]，转为[InjectedParamsDestination]
 * 注意，这样会丢失ScreenDestination子类的行为或属性
 */
open class InjectedParamsDestination<T:Parcelable>(
    val internalParam: T,
    val destination:ScreenDestination): Parameterized<T>, ScreenDestination {
    override val navContext: NavDestinationContext
        get() = if( destination.navContext != EmptyNavContext ) destination.navContext else  NavDestinationContext( internalParam )

    fun toScreen( navigable: Navigable): Screen {
        return destination.toScreen( internalParam,navigable )
    }

    override fun toScreen(params: Parcelable?, navigable: Navigable): Screen {
        return destination.toScreen( params,navigable)
    }

}
/**
 * 把[ScreenDestination]的实例 转为[InjectedParamsDestination]
 * 注意，这样会丢失[ScreenDestination]子类的行为或属性
 * @param params [Parcelable]类型的参数
 */
fun <T:Parcelable> ScreenDestination.injectParams(params: T) :InjectedParamsDestination<T>{
    return InjectedParamsDestination(params,this)
}

