package com.cwl.arccoroutine.test.rv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivitySnapHelperBinding
import com.cwl.arccoroutine.databinding.ItemSnaphelperBinding
import com.cwl.common.widget.recyclerview.multitype2.QuickAdapter2
import com.cwl.common.widget.recyclerview.setupLayoutManager

class SnapHelperActivity : AppCompatActivity() {
    private val viewBinding by viewBinding(ActivitySnapHelperBinding::bind,R.id.root)
    private val texts = arrayListOf<String>("AAA", "BBB", "CCC")
    private val adapter by lazy {
        QuickAdapter2<ItemSnaphelperBinding,String>(ItemSnaphelperBinding::inflate,texts).apply {
            register{vh,s,i->
                with(vh.viewBinding){
                    tvText.text=s
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snap_helper)
        with(viewBinding){
            rvList.adapter=adapter
            rvList.setupLayoutManager(orientation= RecyclerView.HORIZONTAL)
            //PagerSnapHelper 一次滑动一页，效果同vp
            //LinearSnapHelper 滑动停止后item居中对齐
            PagerSnapHelper().attachToRecyclerView(rvList)
        }
    }
}