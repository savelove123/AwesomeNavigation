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

package com.felix.android.navigation

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.felix.android.navigation.ext.MutableLiveEvent
import com.felix.android.navigation.ext.publish
import com.felix.android.navigation.destination.NavDestination
import com.felix.android.navigation.destination.ScreenDestination
import com.felix.android.navigation.model.NavigationCommand
import com.felix.android.navigation.model.NavigationResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.coroutines.suspendCoroutine

/**
 * 表示是一个可以支持导航的组件
 * @param T 表示可以前往的目标页面类型，推荐用密封类来定义这一组[NavDestination]
 */
interface NavComponent<T:NavDestination> :NavCoroutineSupporter{

    fun ScreenDestination.navigate( ){
        router.navigateTo(this)
    }

    suspend fun ScreenDestination.navigateForResult(): NavigationResult{
        resultFlow = MutableSharedFlow( replay = 0)
        return router.navigateForResult(this)
    }

    fun navigateToPrevious( destination: T){
        router.navigateToPrevious( destination )
    }

    fun navigateBack(){
        router.navigateBack()
    }
    fun finish(results: Any? = null){
        router.finish(results)
    }

    val router: MutableLiveEvent<NavigationCommand>

    var activityResultLauncher:ActivityResultLauncher<Intent>?

    var resultFlow : MutableSharedFlow<ActivityResult>?

    fun MutableLiveEvent<NavigationCommand>.navigateTo(
        destination: NavDestination
    ) {
        publish(NavigationCommand.To(destination))
    }

    suspend fun MutableLiveEvent<NavigationCommand>.navigateForResult(destination: NavDestination): NavigationResult =
        suspendCoroutine { continuation ->
            publish(NavigationCommand.ForResult(destination, continuation))
        }

    /**
     * 导航到之前的页面，这里使用T是因为，如果要导航到之前的某个页面
     * 那么之前的[NavDestination]也是当前的目标页面之一。
     */
    fun MutableLiveEvent<NavigationCommand>.navigateToPrevious(destination: T) {
        publish(NavigationCommand.ToPrevious(destination))
    }

    fun MutableLiveEvent<NavigationCommand>.navigateBack() {
        publish(NavigationCommand.Back)
    }

    fun MutableLiveEvent<NavigationCommand>.finish(results: Any? = null) {
        publish(NavigationCommand.Finish(results))
    }
}