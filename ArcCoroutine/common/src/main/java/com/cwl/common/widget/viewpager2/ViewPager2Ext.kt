package com.cwl.common.widget.viewpager2

import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2

/**

 * @Author cwl

 * @Date 2021/1/12 17:40

 */

fun ViewPager2.setupPageTransformer(vararg transformer:ViewPager2.PageTransformer){
    var tsfmr= if(transformer.size==1) transformer[0]
    else{
        CompositePageTransformer().apply {
            transformer.forEach {
                addTransformer(it)
            }
        }
    }
    setPageTransformer(tsfmr)
}

/*一屏显示多个*/
fun ViewPager2.showMultiPage(@Px padding:Int){
    val vp2Ref=this
    (getChildAt(0) as RecyclerView).run {
        if(vp2Ref.orientation==ViewPager2.ORIENTATION_HORIZONTAL) setPadding(padding, 0, padding, 0)
        else setPadding(0, padding, 0, padding)
        clipToPadding = false
    }
}