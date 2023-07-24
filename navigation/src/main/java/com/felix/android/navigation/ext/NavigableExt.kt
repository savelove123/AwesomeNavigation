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
package com.felix.android.navigation.ext

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.felix.android.navigation.Navigable
import com.felix.android.navigation.destination.NavDestinationContext

val EmptyNavContext = NavDestinationContext()

/**
 * 从[Activity]获取[NavDestinationContext]，如果没有就抛出异常.
 */
inline fun < reified T : Parcelable> Activity.requireNavParam(): T =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent?.getParcelableExtra(NavDestinationContext.CONTEXT_PARAM,NavDestinationContext::class.java)
    } else {
        @Suppress("DEPRECATION")
        intent.getParcelableExtra(NavDestinationContext.CONTEXT_PARAM)
    } ?.extra as? T
        ?: illegal("This intent must contain navigation param.")

/**
 * 从[Fragment]获取 [NavDestinationContext]，如果没有就抛出异常
 */
inline fun <reified T : Parcelable> Fragment.requireNavParam(): T =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getParcelable(NavDestinationContext.CONTEXT_PARAM,NavDestinationContext::class.java)
    }else{
        @Suppress("DEPRECATION")
        arguments?.getParcelable(NavDestinationContext.CONTEXT_PARAM)
    }?.extra as? T
        ?: illegal("This bundle must contain navigation param.")
