package com.cwl.customviewbox.UseBehavior.NestedScrolling

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.OverScroller
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat

/**

 * @Author cwl

 * @Date 2021/2/4 11:20

 */
class ChildFlingLayout(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {
    private var child: View? = null
    private val mScroller: OverScroller = OverScroller(context)
    private var mPreMotionX: Int = 0
    private var mPreMotionY: Int = 0
    private val mVelocityTracker: VelocityTracker = VelocityTracker.obtain()

    init {
        isNestedScrollingEnabled=true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        mVelocityTracker.addMovement(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPreMotionX = x
                mPreMotionY = y
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL or ViewCompat.SCROLL_AXIS_VERTICAL)
            }
            MotionEvent.ACTION_MOVE -> {
                var dx = mPreMotionX - x
                var dy = mPreMotionY - y
                //控制滚动边缘
                if (dx < 0) {
                    val range = child!!.width - width
                    if (scrollX + dx < range) {
                        dx = range - scrollX
                    }
                } else {
                    if (scrollX + dx > 0) {
                        dx = -scrollX
                    }
                }

                if (dy < 0) {
                    val range = child!!.height - height
                    if (scrollY + dy < range) {
                        dy = range - scrollY
                    }
                } else {
                    if (scrollY + dy > 0) {
                        dy = -scrollY
                    }
                }
                scrollBy(dx, dy)
                mPreMotionX = x
                mPreMotionY = y

            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker.computeCurrentVelocity(1000)
                val xVelocity = mVelocityTracker.xVelocity
                val yVelocity = mVelocityTracker.yVelocity


                if(dispatchNestedPreFling(xVelocity,yVelocity)){
                    //说明parent处理了
                    //也可以继续处理
                }else{
                    //mScroller.fling(scrollX,scrollY,-xVelocity,-yVelocity,-(width-child!!.width),0,-(height-child!!.height),0)
                    //滑动方向（相当于移动视口位置）和内容滚动方向总是相反的
                    mScroller.fling(
                        scrollX,
                        scrollY,
                        -xVelocity.toInt(),
                        -yVelocity.toInt(),
                        child!!.width - width,
                        0,
                        child!!.height - height,
                        0
                    )
                    invalidate()
                }
                stopNestedScroll()
                mVelocityTracker.clear()
            }
            MotionEvent.ACTION_CANCEL -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
        }
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        child = getChildAt(0)
    }
}