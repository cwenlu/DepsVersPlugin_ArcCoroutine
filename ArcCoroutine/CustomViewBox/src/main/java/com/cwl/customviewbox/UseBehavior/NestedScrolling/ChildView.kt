package com.cwl.customviewbox.UseBehavior.NestedScrolling

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import androidx.core.view.ViewConfigurationCompat

/**

 * @Author cwl

 * @Date 2021/2/3 13:28
 5.0以前兼容才需要实现NestedScrollingChild3，NestedScrollingParent3
 只实现了3中新增功能，其他的用的view的
 */
class ChildView(context: Context,attrs:AttributeSet):View(context,attrs) {
    private var mPreMotionX:Float=0f
    private var mPreMotionY:Float=0f
    private val consumed=IntArray(2)
    private val offsetInWindow=IntArray(2)

    init {
        isNestedScrollingEnabled=true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x=event.x
        val y=event.y
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                mPreMotionX=x
                mPreMotionY=y
                //开始滑动
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL or ViewCompat.SCROLL_AXIS_VERTICAL )
            }
            MotionEvent.ACTION_MOVE->{
                var dx=(x-mPreMotionX).toInt()
                var dy=(y-mPreMotionY).toInt()

                //分发给parent
                if(dispatchNestedPreScroll(dx,dy,consumed,offsetInWindow)){
                    dx-=consumed[0]
                    dy-=consumed[1]
                }

                ViewCompat.offsetLeftAndRight(this,dx)
                ViewCompat.offsetTopAndBottom(this,dy)
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                stopNestedScroll()
            }
        }
        return true
    }

}