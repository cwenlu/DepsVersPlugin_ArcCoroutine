package com.cwl.customviewbox.UseBehavior

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.cwl.common.exts.toColor
import com.cwl.customviewbox.R

/**

 * @Author cwl

 * @Date 2021/2/6 11:01

 */
class SimpleBehavior(context: Context,attributeSet: AttributeSet):CoordinatorLayout.Behavior<View>(context,attributeSet) {
    private val mArgbEvaluator=ArgbEvaluator()
    //至少调用一次
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        return dependency is TextView
    }

    //初始至少调用一次
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val topOffset=dependency.top-child.top
        ViewCompat.offsetTopAndBottom(child,topOffset)

        val fraction=dependency.y/(parent.height-dependency.height)
        val evaluate = mArgbEvaluator.evaluate(
            fraction,
            Color.parseColor("#D81B60"),
            Color.parseColor("#03DAC6")
        )
        child.setBackgroundColor(evaluate as Int)
        println("${child.left}---onDependentViewChanged")

        //不想重写onLayoutChild,一些简单的调整可以在这处理
//        child.x=dependency.measuredWidth+200f
        return true
    }

    //layoutDependsOn设置依赖 onLayoutChild--->onDependentViewChanged--->onLayoutChild
    //如果是通过anchor设置依赖 onLayoutChild--->onDependentViewChanged--->onLayoutChild--->onDependentViewChanged
    //anchor时,为啥最后onLayoutChild的设置没有影响到onDependentViewChanged还没搞清楚
    //所以anchor方式要在onDependentViewChanged处理定位
    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        val dependencies = parent.getDependencies(child)
        if(dependencies.size==1){
            //可以通过dependency 来定位child位置
            val left=dependencies[0].measuredWidth+200
            val right=left+child.measuredWidth
            val bottom=dependencies[0].measuredHeight
            child.layout(left,0,right,bottom)
            println("${child.left}---onLayoutChild")
            return true
        }
        return true
//        return super.onLayoutChild(parent, child, layoutDirection)
    }
}

class TouchBehavior(context: Context,attributeSet: AttributeSet):CoordinatorLayout.Behavior<View>(context,attributeSet){
    var py = 0f
    //本质是CoordinatorLayout的onTouchEvent分发
    override fun onTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                py = y
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = y - py
                py=y
                ViewCompat.offsetTopAndBottom(child, dy.toInt())
            }
        }
        return true
    }
}