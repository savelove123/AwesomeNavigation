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
package com.felix.android.navigation.model

import com.felix.android.navigation.destination.NavDestination
import kotlin.coroutines.Continuation
import com.felix.android.navigation.NavComponent
/**
 * 一个简单的密封类用于更优雅地从[NavComponent]进行导航
 * [NavigationCommand]中定义了一组已经定义的导航的行为
 */
sealed class NavigationCommand {

    data class ForResult(
        val destination: NavDestination,
        val continuation: Continuation<NavigationResult>
    ) : NavigationCommand()

    data class To(val destination: NavDestination) : NavigationCommand()

    data class ToPrevious(val destination: NavDestination) : NavigationCommand()

    object Back : NavigationCommand()

    data class Finish(val results: Any?) : NavigationCommand()
}
