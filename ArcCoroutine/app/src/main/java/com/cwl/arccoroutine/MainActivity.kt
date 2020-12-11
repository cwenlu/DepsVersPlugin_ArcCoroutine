package com.cwl.arccoroutine

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.cwl.arccoroutine.itemtouchhelper.UseItemTouchHelperActivity
import com.cwl.common.exts.*
import com.cwl.common.widget.recyclerview.multitype.MultiTypeAdapter
import com.cwl.common.widget.recyclerview.multitype.QuickAdapter


class MainActivity : AppCompatActivity(){


    var listData = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        listData.add("UseItemTouchHelper")
        val recyclerView = RecyclerView(this)
        recyclerView.setupLayoutManager(3)
        recyclerView.gridDivider(2,Color.CYAN)
        var multiTypeAdapter = QuickAdapter(listData)
        multiTypeAdapter.register<String>(android.R.layout.simple_list_item_1){holder,t,_->
            (holder.containerView as TextView).text=t
        }
        recyclerView.onItemClick { viewHolder, i ->
            when(i){
                0->showActivity(UseItemTouchHelperActivity::class)

            }
        }
        recyclerView.adapter=multiTypeAdapter
        setContentView(recyclerView)

    }
}
