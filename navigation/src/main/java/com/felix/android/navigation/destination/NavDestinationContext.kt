package com.felix.android.navigation.destination
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

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 为[ScreenDestination]提供更多的上下文的快捷入口
 *
 */
@Parcelize
data class NavDestinationContext(
    val extra: Parcelable = Bundle.EMPTY
) : Parcelable {
    companion object {
        const val CONTEXT_PARAM = "NAV_DIRECTION_CONTEXT_PARAM"
    }
}
