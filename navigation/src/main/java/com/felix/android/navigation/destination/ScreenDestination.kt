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

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.ext.EmptyNavContext
import com.felix.android.navigation.ext.illegal

typealias Screen = Any

interface ScreenDestination : NavDestination {

    /**
     * 页面目的地的上下文
     */
    val navContext: NavDestinationContext
        get() = EmptyNavContext

    /**
     * 前去导航的目的地
     * 目前只支持[Activity]、[Fragment]、[DialogFragment]
     */
    fun toScreen(params:Parcelable?,
                 navigable: Navigable = currentLocation() ?: illegal("Please call toScreen() in Navigable")): Screen

    /**
     * 当前所在的位置,如果没有，在Navigable会自动注入
     */
    fun currentLocation():Navigable?{
        return null
    }

    fun Navigable.requireContext() = (currentLocation() as? Context)?:illegal("The current ScreenDestination object needs to call navigate in an Context.")
    fun Navigable.requireActivity() = (currentLocation() as? Activity)?:illegal("The current ScreenDestination object needs to call navigate in an Activity")

}


