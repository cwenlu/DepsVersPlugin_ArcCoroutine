package com.cwl.arccoroutine.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwl.common.okhttp.rb
import kotlinx.coroutines.launch
import timber.log.Timber

class TestVM :ViewModel(){
    fun dowload(){
        viewModelScope.launch{
            "http://oss.pgyer.com/51d9564b8c702258995d9fc981e74904.apk?auth_key=1565435999-f4a35ee45278f6d2a8e017e2df79670f-0-53cd99c3465f0cc20ed3900fcda87528&response-content-disposition=attachment%3B+filename%3DBimSZ13-1.0.9.apk"
                .rb().dowloadAwait{ currentLength, totalLength, percent->
                    Timber.i("${currentLength},${totalLength},${percent}")
                }

        }
    }

    fun baidu(){
        viewModelScope.launch() {
            "http://www.baidu.com/".rb().getAwait<String>()
        }
    }

    fun login(){
        viewModelScope.launch {
            "login".rb()
                .params("j_type" to "mobile", "j_username" to "100000", "j_password" to "999999")
                .postAwait<String>()
        }
    }


}