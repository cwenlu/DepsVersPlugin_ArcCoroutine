package com.cwl.arccoroutine.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityBehaviorUseBinding
import by.kirich1409.viewbindingdelegate.viewBinding


class BehaviorUseActivity : AppCompatActivity() {
    private val viewBinding by viewBinding(ActivityBehaviorUseBinding::bind,R.id.root)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_behavior_use)
        with(viewBinding){

        }
    }
}