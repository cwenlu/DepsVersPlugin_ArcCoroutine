package com.cwl.arccoroutine.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivitySimpleBehaviorBinding

class SimpleBehaviorActivity : AppCompatActivity() {
    private val vb by viewBinding(ActivitySimpleBehaviorBinding::bind, R.id.root)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_behavior)
        var py = 0f
        with(vb) {
//            (child.layoutParams as CoordinatorLayout.LayoutParams).anchorId=R.id.tv_anchor

//            tvAnchor.setOnTouchListener { view, motionEvent ->
//                val y = motionEvent.y
//                when (motionEvent.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        py = y
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        val dy = y - py
//                        //这里不需要是因为是相对tvAnchor获取的坐标,而移动却是相对最外层布局,无形把比例变小，所以拖动正常
//                        //py=y
//                        ViewCompat.offsetTopAndBottom(view, dy.toInt())
//                    }
//                }
//                true
//            }

        }
    }

}