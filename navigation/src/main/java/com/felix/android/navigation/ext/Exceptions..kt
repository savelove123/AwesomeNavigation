package com.felix.android.navigation.ext

/** 抛出一个带有信息的 [IllegalStateException]  */
fun illegal(errorMessage: String? = null): Nothing = throw IllegalStateException(errorMessage)

/** 抛出一个带有信息的 [UnsupportedOperationException] */
fun unsupported(errorMessage: String? = null): Nothing =
    throw UnsupportedOperationException(errorMessage)
