package com.cwl.arccoroutine.wanandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityWanMainBinding

class WanMainActivity : AppCompatActivity(R.layout.activity_wan_main) {
    private val viewBinding by viewBinding(ActivityWanMainBinding::bind,R.id.root)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}


//简单演示基于koin非反射方式进行一个封装
typealias Bind=(view: View)->ViewBinding
abstract class BaseActivity<VB:ViewBinding>(var bind:Bind, var rooId:Int): AppCompatActivity() {
    private val viewBinding by viewBinding(bind,rooId)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (viewBinding as VB).vb()
    }
    abstract fun VB.vb()

}

class TestActivity : BaseActivity<ActivityWanMainBinding>(ActivityWanMainBinding::bind,R.id.root){
    override fun ActivityWanMainBinding.vb() {
        test
    }
}