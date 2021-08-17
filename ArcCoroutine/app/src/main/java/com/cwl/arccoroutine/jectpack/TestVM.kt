package com.cwl.arccoroutine.jectpack

import androidx.lifecycle.*
import com.cwl.common.okhttp.rb
import kotlinx.coroutines.Dispatchers
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

    //==========================================

    var page=MutableLiveData<Int>(1)

    fun updatePage(){
        page.value=2
    }

    //这2个转化方法内部都是MediatorLiveData
    //pageStr pageStr2如果没有观察者是不能执行map switchMap中的代码块的
    //eg: Activity中observe 后其实添加了LifecycleBoundObserver#onStateChanged 会 activeStateChanged更新 mActiveCount
    // 然后才能在活动状态下onActive MediatorLiveData里面会添加之前设置的observe
    val pageStr=page.map {
        println("${Thread.currentThread()}==$it")
        "page-$it"
    }

    val pageStr2:LiveData<String> =page.switchMap {
        println("${Thread.currentThread()}==$it")

        MutableLiveData("s-$it")
    }

}
