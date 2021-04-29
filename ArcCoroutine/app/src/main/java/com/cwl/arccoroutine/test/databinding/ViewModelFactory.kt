package com.cwl.arccoroutine.test.databinding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

/**

 * @Author cwl

 * @Date 2021/4/29 10:21
    不用koin 原生构造factory，再配合 viewModels使用
 */
class ViewModelFactory(owner:SavedStateRegistryOwner,defaultArgs:Bundle?=null): AbstractSavedStateViewModelFactory(owner,defaultArgs) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T =with(modelClass){
        //无参可以这样省略写
        when{
            isAssignableFrom(DataBindingHotArticlesViewModel::class.java)->DataBindingHotArticlesViewModel()
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}

//fun Fragment.getViewModelFactory(): ViewModelProvider.Factory{
////    requireContext()
//}

fun ComponentActivity.getViewModelFactory()=ViewModelFactory(this)