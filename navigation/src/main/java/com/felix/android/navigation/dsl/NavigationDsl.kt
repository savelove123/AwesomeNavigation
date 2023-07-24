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

package com.felix.android.navigation.dsl
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.destination.ScreenDestination
import com.felix.android.navigation.ext.isFragment
import com.felix.android.navigation.ext.isFragmentDialog
import com.felix.android.navigation.ext.isIntent

internal typealias ScreenBlock = (ScreenDestination.() -> Unit)


fun NavDestination.doWhen(
    block: (NavigationDsl.() -> OtherDestinationDsl)
) = block(NavigationDsl(this))

object OtherDestinationDsl


class NavigationDsl(
    private val destination: NavDestination
    ) {

    // region Data

    private var isFragmentDialog: ScreenBlock? = null
    private var isFragment: ScreenBlock? = null
    private var isIntent: ScreenBlock? = null
    private lateinit var otherDestination: () -> Unit

    // endregion

    // region Dsl

    fun isFragmentDialog(block: ScreenBlock) = this.apply { isFragmentDialog = block }
    fun isFragment(block: ScreenBlock) = this.apply { isFragment = block }
    fun isIntent(block: ScreenBlock) = this.apply { isIntent = block }
    fun otherDestination(block: () -> Unit) = this.apply { otherDestination = block }
        .run { build() }
        .let { OtherDestinationDsl }

    // endregion

    // region DSL builder

    private fun build() {
        when {
            destination.isFragmentDialog() -> {
                isFragmentDialog?.invoke( destination as ScreenDestination) ?: otherDestination()
            }
            destination.isFragment() -> {
                isFragment?.invoke(destination as ScreenDestination) ?: otherDestination()
            }
            destination.isIntent() -> {
                isIntent?.invoke(destination as ScreenDestination) ?: otherDestination()
            }

            else -> otherDestination()
        }
    }

    // endregion
}