package com.felix.android.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel

abstract class BaseVM( application: Application ):AndroidViewModel( application){

    abstract fun attachViewModel( savedInstanceState: Bundle?)
}