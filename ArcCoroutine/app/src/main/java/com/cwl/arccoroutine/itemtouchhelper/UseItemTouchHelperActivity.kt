package com.cwl.arccoroutine.itemtouchhelper

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cwl.common.exts.*
import com.cwl.common.widget.recyclerview.itemtouch.SimpleItemTouchHelperCallback
import com.cwl.common.widget.recyclerview.multitype.MultiTypeAdapter
import com.cwl.common.widget.recyclerview.multitype.QuickAdapter

class UseItemTouchHelperActivity : AppCompatActivity() {
    var listData = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_use_item_touch_helper)
        listData.addAll((1..50).map { "item->$it" })
        val recyclerView = RecyclerView(this)
        var itemTouchAdapter = QuickAdapter(listData)
        var setupItemTouchHelper =
            recyclerView.setupItemTouchHelper(object : SimpleItemTouchHelperCallback(itemTouchAdapter) {
                override fun isLongPressDragEnabled(): Boolean {
//                return super.isLongPressDragEnabled()
                    return false
                }
            })

        itemTouchAdapter.register<String>(android.R.layout.simple_list_item_1){ holder, t, _->
            (holder.containerView as TextView).apply {
                text=t
                setBackgroundColor(Color.GRAY)
                setOnTouchListener { view, motionEvent ->
                    if(motionEvent.action==MotionEvent.ACTION_DOWN){
                        setupItemTouchHelper.startDrag(holder)
                    }
                    false
                }
            }

        }

        setContentView(recyclerView.apply{
            adapter=itemTouchAdapter
            setupLayoutManager()
        })

//        直接采用默认的长按触发拖动
//        setContentView(recyclerView.apply {
//            adapter=itemTouchAdapter
//            setupItemTouchHelper(itemTouchAdapter)
//            setupLayoutManager()
////            divider(2, Color.RED)
//        })


    }
}
