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

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.felix.android.navigation.Parameterized
import com.felix.android.navigation.destination.InjectedParamsDestination
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.destination.NavDestinationContext
import com.felix.android.navigation.destination.ScreenDestination

internal fun NavDestination.isIntent() =
    (this as? ScreenDestination)?.toScreen(getParams()) is Intent

internal fun NavDestination.isFragment() =
    (this as? ScreenDestination)?.toScreen(getParams()) is Fragment

internal fun NavDestination.isFragmentDialog() = run {
    (this as? ScreenDestination)?.toScreen(getParams()) is DialogFragment
}

fun NavDestination.getParams() = if( this is InjectedParamsDestination<*>) this.internalParam else null

fun NavDestination.requireIntent() =
    when (this) {
        is ScreenDestination -> toIntentScreen{
            setNavContextParam(this@requireIntent)
        }
        else -> null
    } ?: illegal("This destination doesn't support Intent")

fun NavDestination.requireFragment(): Fragment =
    when (this) {
        is ScreenDestination -> toFragmentScreen {
            arguments = (arguments ?: bundleOf()).run {
                setNavContextParam(this@requireFragment)
            }
        }
        else -> null
    } ?: illegal("This destination doesn't support Fragment")

fun NavDestination.requireDialogFragment(): DialogFragment =
    requireFragment() as DialogFragment


private fun ScreenDestination.toIntentScreen(block: Intent.() -> Unit): Intent? =
    (toScreen(getParams() ) as? Intent)?.apply(block)

private fun ScreenDestination.toFragmentScreen(block: Fragment.() -> Unit): Fragment? =
    (toScreen(getParams()) as? Fragment)?.apply(block)

private fun Bundle.setNavContextParam(destination: ScreenDestination) = apply {
    putParcelable(NavDestinationContext.CONTEXT_PARAM, destination.navContext)
}

private fun Intent.setNavContextParam(destination: ScreenDestination) = apply {
    destination.navContext
        .takeIf { it != EmptyNavContext }
        ?.let {
            putExtra(NavDestinationContext.CONTEXT_PARAM, destination.navContext)
        }
}

// endregion
