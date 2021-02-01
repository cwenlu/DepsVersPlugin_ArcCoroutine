package com.cwl.arccoroutine.wanandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityWanMainBinding

class WanMainActivity : AppCompatActivity(R.layout.activity_wan_main) {
    private val viewBinding by viewBinding(ActivityWanMainBinding::bind,R.id.root)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}