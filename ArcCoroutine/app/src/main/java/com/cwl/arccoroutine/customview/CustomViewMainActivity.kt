package com.cwl.arccoroutine.customview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cwl.arccoroutine.showActivity
import com.cwl.arccoroutine.test.paging.ui.PagingActivity
import com.cwl.arccoroutine.test.rv.SnapHelperActivity
import com.cwl.arccoroutine.test.rv.UseItemTouchHelperActivity
import com.cwl.arccoroutine.test.viewpager2.ViewPager2MainActivity
import com.cwl.arccoroutine.wanandroid.ui.hotarticles.HotArticlesActivity
import com.cwl.arccoroutine.wanandroid.ui.hotarticles.HotArticlesPagingActivity
import com.cwl.arccoroutine.wanandroid.ui.hotarticles.HotArticlesPagingActivity2
import com.cwl.common.widget.recyclerview.gridDivider
import com.cwl.common.widget.recyclerview.multitype.QuickAdapter
import com.cwl.common.widget.recyclerview.onItemClick
import com.cwl.common.widget.recyclerview.setupLayoutManager

class CustomViewMainActivity : AppCompatActivity() {
    var listData = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listData.add("ViewDrag")
        listData.add("ViewDrag1")
        listData.add("Behavior")
        listData.add("SimpleNestedScrolling")
        listData.add("SimpleScroller")
        listData.add("NestedFling")
        listData.add("SimpleBehavior")
        listData.add("ScrollBehavior")
        listData.add("FlowLayoutManager")

        val recyclerView = RecyclerView(this)
        recyclerView.setupLayoutManager(3)
        recyclerView.gridDivider(2, Color.CYAN)
        var multiTypeAdapter = QuickAdapter(listData)
        multiTypeAdapter.register(android.R.layout.simple_list_item_1){holder,t,_->
            (holder.containerView as TextView).text=t
        }
        recyclerView.onItemClick { viewHolder, i ->
            when(i){
                0->showActivity(SimpleUseViewDragHelperActivity::class)
                1->showActivity(SimpleUseDragHelper1Activity::class)
                2->showActivity(BehaviorUseActivity::class)
                3->showActivity(SimpleNestedScrollingActivity::class)
                4->showActivity(SimpleScollerLayoutActivity::class)
                5->showActivity(NestedFlingActivity::class)
                6->showActivity(SimpleBehaviorActivity::class)
                7->showActivity(ScrollBehaviorActivity::class)
                8->showActivity(LayoutManagerTestActivity::class)

            }
        }
        recyclerView.adapter=multiTypeAdapter
        setContentView(recyclerView)


    }
}