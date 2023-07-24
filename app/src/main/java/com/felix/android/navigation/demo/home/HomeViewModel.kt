package com.felix.android.navigation.demo.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "测试占位页面，第三个页面前往登录"
    }
    val text: LiveData<String> = _text
}