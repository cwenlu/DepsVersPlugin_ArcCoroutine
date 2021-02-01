package com.cwl.arccoroutine.test.viewpager2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityViewPager2MainBinding
import com.cwl.arccoroutine.databinding.ItemPageBinding
import com.cwl.common.widget.recyclerview.multitype2.QuickAdapter2

class ViewPager2MainActivity : AppCompatActivity() {
    private val viewBinding by viewBinding(ActivityViewPager2MainBinding::bind, R.id.root)
    private val texts = arrayListOf<String>("AAA", "BBB", "CCC")
    private val adapter by lazy {
        QuickAdapter2<ItemPageBinding,String>(ItemPageBinding::inflate, texts).apply {
            register { commonViewHolder2, s, i ->
                with(commonViewHolder2.viewBinding) {
                    tvText.text = s
                    tvText.setBackgroundColor(
                        when (i) {
                            0 -> Color.RED
                            1 -> Color.GREEN
                            else -> Color.BLUE
                        }
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager2_main)
        with(viewBinding) {
            vp2.adapter = adapter
//            vp2.isUserInputEnabled=false //禁止滑动

            val recyclerView= vp2.getChildAt(0) as RecyclerView
            vp2.offscreenPageLimit=2
            recyclerView.apply {
                val padding = 10 + 10
                // setting padding on inner RecyclerView puts overscroll effect in the right place
                setPadding(padding*4, 0, padding*4, 0)
                clipToPadding = false
            }

//            vp2.setPageTransformer(MarginPageTransformer(10))
            val compositePageTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(10))
//                addTransformer(ZoomInTransformer())
                addTransformer(ZoomOutPageTransformer())
            }
            vp2.setPageTransformer(compositePageTransformer)
            vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            })
            btnChange.setOnClickListener {
                vp2.orientation =
                    if (vp2.orientation == ViewPager2.ORIENTATION_HORIZONTAL) ViewPager2.ORIENTATION_VERTICAL else ViewPager2.ORIENTATION_HORIZONTAL
            }
        }
    }
}