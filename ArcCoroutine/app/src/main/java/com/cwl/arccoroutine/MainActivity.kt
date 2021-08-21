package com.cwl.arccoroutine

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.cwl.arccoroutine.customview.CustomViewMainActivity
import com.cwl.arccoroutine.jectpack.TestActivity
import com.cwl.arccoroutine.jectpack.databinding.DataBindingActivity
import com.cwl.arccoroutine.jectpack.hilt.HiltActivity
import com.cwl.arccoroutine.jectpack.rv.UseItemTouchHelperActivity
import com.cwl.arccoroutine.jectpack.paging.ui.PagingActivity
import com.cwl.arccoroutine.jectpack.rv.SnapHelperActivity
import com.cwl.arccoroutine.jectpack.viewpager2.ViewPager2MainActivity
import com.cwl.arccoroutine.wanandroid.ui.hotarticles.HotArticlesActivity
import com.cwl.arccoroutine.wanandroid.ui.hotarticles.HotArticlesPagingActivity
import com.cwl.arccoroutine.wanandroid.ui.hotarticles.HotArticlesPagingActivity2
import com.cwl.common.widget.recyclerview.gridDivider
import com.cwl.common.widget.recyclerview.multitype.QuickAdapter
import com.cwl.common.widget.recyclerview.onItemClick
import com.cwl.common.widget.recyclerview.setupLayoutManager


class MainActivity : AppCompatActivity(){


    var listData = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        listData.add("UseItemTouchHelper")
        listData.add("Paging3.x")
        listData.add("vm+vb")
        listData.add("Paging+vb")
        listData.add("Paging+vb+封装adater")
        listData.add("viewpager2")
        listData.add("snaphelper")
        listData.add("customview")

        listData.add("databinding")
        listData.add("hilt")
        listData.add("test")
        val recyclerView = RecyclerView(this)
        recyclerView.setupLayoutManager(3)
        recyclerView.gridDivider(2,Color.CYAN)
        var multiTypeAdapter = QuickAdapter(listData)
        multiTypeAdapter.register(android.R.layout.simple_list_item_1){holder,t,_->
            (holder.containerView as TextView).text=t
        }
        recyclerView.onItemClick { viewHolder, i ->
            when(i){
                0->showActivity(UseItemTouchHelperActivity::class)
                1->showActivity(PagingActivity::class)
                2->showActivity(HotArticlesActivity::class)
                3->showActivity(HotArticlesPagingActivity::class)
                4->showActivity(HotArticlesPagingActivity2::class)
                5->showActivity(ViewPager2MainActivity::class)
                6->showActivity(SnapHelperActivity::class)
                7->showActivity(CustomViewMainActivity::class)

                8->showActivity(DataBindingActivity::class)
                9->showActivity(HiltActivity::class)
                10->showActivity(TestActivity::class)

            }
        }
        recyclerView.adapter=multiTypeAdapter
        setContentView(recyclerView)


    }

}
