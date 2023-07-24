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

import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.FragmentActivity
import com.felix.android.navigation.ext.illegal
import com.felix.android.navigation.Navigable
import kotlinx.parcelize.Parcelize

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

/**
 * [FragmentActivity] 返回的结果类型，这个 [FragmentActivity] 必须是通过 [Navigable.navigateToForResult] 启动。
 * @property [code]： [FragmentActivity] 执行完成的的code，例如 [Activity.RESULT_OK]。
 * @property [data]： [FragmentActivity] 可能会返回的数据。
 */
@Suppress("unused")
data class NavigationResult(
    val code: Int,
    val data: Intent?
) {

    inline fun <reified T : Parcelable> getParcelable(): T?=
        if( data == null ){
             null
        }else {
                data.parcelableArrayList<T>(RESULT_PARAM)
                ?.toList()
                ?.first()
                ?: illegal("The data doesn't contain any extra.")
        }

    inline fun <reified T : Parcelable> getParcelables(): List<T> =
        if( data == null ){
             emptyList()
        }else {
            data.parcelableArrayList<T>(RESULT_PARAM)
                ?.toList()
                ?: illegal("The data doesn't contain any extra.")
        }

    val hasBeenCanceled: Boolean
        get() = code == Activity.RESULT_CANCELED

    val hasData: Boolean
        get() = code == Activity.RESULT_OK

    @Parcelize
    internal object SuccessWithNoResult : Parcelable

    companion object {
        const val RESULT_PARAM = "RESULT_PARAM"
    }
}
