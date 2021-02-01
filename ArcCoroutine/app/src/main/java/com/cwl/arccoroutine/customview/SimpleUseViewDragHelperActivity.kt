package com.cwl.arccoroutine.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivitySimpleUseViewDragHelperBinding
import com.cwl.common.exts.dp2px
import com.cwl.customviewbox.UseViewDragHelper.SimpleUseViewDragHelper1

class SimpleUseViewDragHelperActivity : AppCompatActivity() {
    private val viewBinding by viewBinding(ActivitySimpleUseViewDragHelperBinding::bind,R.id.root)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_use_view_drag_helper)

        with(viewBinding){
            if(root is SimpleUseViewDragHelper1){
//                root.setDragView(tvDrag)
                root.setScrollableView(sv)
                root.setPeekHeight(dp2px(47f))
            }
        }
    }
}