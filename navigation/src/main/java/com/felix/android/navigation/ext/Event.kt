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


class Event<out T>(
    private val content: T? = null
) {

    var consumed = false
        private set // Allow external read but not write

    fun consume(): T? {
        return if (consumed) {
            null
        } else {
            consumed = true
            content
        }
    }

    /**
     * Consumes the content if it's not been consumed yet and run the block [block].
     */
    private fun consumeAndRun(block: (T?) -> Unit) {
        if (!consumed) {
            block(consume())
        }
    }

    fun consumeAndRunNonNull(block: (T) -> Unit) {
        consumeAndRun { if (it != null) block(it) }
    }

    /**
     * @return The content whether it's been handled or not.
     */
    fun peek(): T? = content

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event<*>

        if (content != other.content) return false
        if (consumed != other.consumed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content?.hashCode() ?: 0
        result = 31 * result + consumed.hashCode()
        return result
    }
}
