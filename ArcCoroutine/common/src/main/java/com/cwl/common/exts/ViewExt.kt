package com.cwl.common.exts

import android.view.View

/**

 * @Author cwl

 * @Date 2021/1/29 11:31

 */

/**
 * view在给定的x,y下返回true
 * view 和提供x的MotionEvent必须子父关系, 不能隔代
 *
 * X position to test in the parent's coordinate system 父坐标系中的值
 */
fun View.isViewUnder(x:Int,y:Int):Boolean{
//    return x>=left && x<=right && y>=top && y<=bottom
    return x in left .. right && y in top .. bottom
}

//androidx.core.view 中提供了一些操作
//val View.isGone get() = visibility==View.GONE

inline val View.isVisibility get() = visibility==View.VISIBLE